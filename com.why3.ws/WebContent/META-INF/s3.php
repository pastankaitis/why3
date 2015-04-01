<?php

// This software code is made available "AS IS" without warranties of any
// kind.  You may copy, display, modify and redistribute the software
// code either by itself or as incorporated into your code; provided that
// you do not remove any proprietary notices.  Your use of this software
// code is at your own risk and you waive any claim against Amazon
// Digital Services, Inc. or its affiliates with respect to your use of
// this software code. (c) 2006 Amazon Digital Services, Inc. or its
// affiliates.

// buffer output so that we can set content-type header later on
ob_start();

// See $introMessage below.
// Notes:
// - This relies on HTTP_Request from pear.php.net, but the latest version
//   has a bug; see note below on how to fix it (one-character change).
// - A real implementation would stream data in and out of S3; this
//   proof-of-concept stores complete files/responses on the PHP server
//   before passing them on to Amazon or to the web browser.
// - Because of the above fact, large files will require increasing PHP's
//   various settings governing the size of uploaded files.



$introMessage = "This file is a proof-of-concept of how to use Amazon S3 via PHP.  It implements object PUT/GET/DELETE, bucket PUT(create)/GET(list)/DELETE, and service GET(list all my buckets).  It sends your secret access key from your browser to your PHP server so <b>only use this over SSL or on a trusted network!</b>";

$S3_URL = "http://s3.amazonaws.com/";

// grab this with "pear install Crypt_HMAC"
require_once 'Crypt/HMAC.php';

// grab this with "pear install --onlyreqdeps HTTP_Request"
require_once 'HTTP/Request.php';
// Note that version HTTP_Request 1.3.0 has a BUG in it!  Change line
// 765 from:
//            (HTTP_REQUEST_METHOD_POST != $this->_method && empty($this->_postData) && empty($this->_postFiles))) {
// to:
//            (HTTP_REQUEST_METHOD_POST == $this->_method && empty($this->_postData) && empty($this->_postFiles))) {
// Without this change PUTs with non-empty content-type will fail!

// generic form fields
$fields = array("AWSAccessKeyId" => "keyId",
                "SecretAccessKey" => "secretKey",
                "Bucket (no slashes)" => "bucket",
                "Key" => "key",
                "Content-Type" => "contentType");
// submit button value
$submit = $_REQUEST['submit'];

if ($submit =="")  {
    // The simplest case -- just draw the input form
?>

<html>
  <head>
    <script type="text/javascript" language="text/javascript">
      <!--
          function showPutFields() { 
            document.getElementById("putFile").style.visibility = 'visible'; 
            document.getElementById("putCT").style.visibility = 'visible'; 
            return false; 
          }
          function hidePutFields() { 
            document.getElementById("putFile").style.visibility = 'hidden'; 
            document.getElementById("putCT").style.visibility = 'hidden'; 
            document.getElementById("fileName").value = ''; 
            return false; 
          }
          function putVisibility() { 
            if (document.getElementById("verb").options.selectedIndex == 1) 
              showPutFields(); 
            else 
              hidePutFields(); 
            return false;
          }
        -->
    </script>
    <title>S3/PHP proof-of-concept</title>
  </head>
  <body>
    <h1>S3/PHP proof-of-concept</h1>
    <? echo $introMessage ?>
    <hr/>
    <h1/>
    <form action="s3.php" method="POST" enctype="multipart/form-data">
      <table>
<?php
    foreach ($fields as $k => $v) {
        print "<tr><td>$k:</td><td><input name=\"$v\" value=\"\"/></td></tr>";
    }
?>
        <tr>
          <td>Operation:</td>
          <td>
              <select id="verb" onchange="putVisibility();" name="verb">
                <option value="GET">GET</option>
                <option value="PUT">PUT</option>
                <option value="DELETE">DELETE</option>
              </select>
          </td>
        </tr> 
        <tr id="putCT" style="visibility:hidden;">
          <td>Access Control Policy (PUT only):</td>
          <td>
            <select name="acl">
              <option value="private">private</option>
              <option value="public-read">public-read</option>
              <option value="public-read-write">public-read-write</option>
            </select>
          </td>
        </tr>
        <tr id="putFile" style="visibility:hidden;">
          <td colspan="2">File to PUT (PUT only): <input id="fileName" type="file" name="file"/></td>
        </tr>
      </table>
      <input type="submit" name="submit" value="Go!"/>
    </form>

<?php
    ob_end_flush();

} elseif ($submit == "Go!") {
    // User has entered parameters so let's do the S3 request!

    // pull off request parameters.
    $keyId = $_REQUEST['keyId'];
    $secretKey = $_REQUEST['secretKey'];
    $verb = $_REQUEST['verb'];
    $acl = $_REQUEST['acl'];
    $contentType = $_REQUEST['contentType'];
    $resource = $_REQUEST['bucket'] . "/" . $_REQUEST['key'];
    if ($resource == "/") {
        $resource = "";
    }

    if ($verb == "PUT") {
        if ($_REQUEST['key'] == "") {
            $_FILES['file']['tmp_name'] = "";
        }
        if ($_FILES['file']['tmp_name'] == "" && $_REQUEST['key']!="") {
            error("Must specify a file to use with object PUT");
        }
        if (is_uploaded_file($_FILES['file']['tmp_name'])) {
            $filePath = $_FILES['file']['tmp_name'];
        } elseif ($_FILES['file']['tmp_name'] != "") {
            error("Serious error");
        }
    } else {
        if ($_FILES['file']['tmp_name'] != "") {
            error("Must not specify a file to use with anything but PUT");
        }
    }

    $methods = array("GET"=>1, "DELETE"=>1, "PUT"=>1);
    if ($methods[$verb] == 0) {
        error("Unknown verb $verb");
    }

    $httpDate = gmdate("D, d M Y H:i:s T");
    $stringToSign = "$verb\n\n$contentType\n$httpDate\nx-amz-acl:$acl\n/$resource";
    $hasher =& new Crypt_HMAC($secretKey, "sha1");
    $signature = hex2b64($hasher->hash($stringToSign));
    //    error("[$stringToSign,$signature]");

    $req =& new HTTP_Request($S3_URL . $resource);
    $req->setMethod($verb);
    $req->addHeader("content-type", $contentType);
    $req->addHeader("Date", $httpDate);
    $req->addHeader("x-amz-acl", $acl);
    $req->addHeader("Authorization", "AWS " . $keyId . ":" . $signature);
    if ($filePath != "") {
        $req->setBody(file_get_contents($filePath));
    }
    $req->sendRequest();

    $ct = $req->getResponseHeader("content-type");
    if ($ct == "application/xml") $ct = "text/xml";
    header("content-type: $ct");
    ob_end_flush();

    if ($req->getResponseCode() >= 300) {
        print $req->getResponseBody();
        return;
    }

    if ($verb != "GET") {
        print "$resource ${verb}ed successfully.";
        return;
    }

    print $req->getResponseBody();

} else {
    print "Unknown submit! [$submit]";
}

die;

function hex2b64($str) {
    $raw = '';
    for ($i=0; $i < strlen($str); $i+=2) {
        $raw .= chr(hexdec(substr($str, $i, 2)));
    }
    return base64_encode($raw);
}

function error($str) {
    print "<div style='color: red;'><h2><pre>$str</pre></h2></div>";
    die;
}

function dump($var) {
    print "<pre>";
    print_r($var);
    print "</pre>";
}
?>
