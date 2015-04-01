package uk.ac.ncl.eventb.why3.prefpage.scen;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class ScenarioRegistry
{
    private static ScenarioRegistry registryInstance;
    
    public static ScenarioRegistry getInstance()
    {
        if (registryInstance == null)
        {
            registryInstance = new ScenarioRegistry();
            registryInstance.loadFromPreferences();
        }
        return registryInstance;
    }
    
    private Map<String, String> _registry = new HashMap<String, String>();
    private String _accessedNull;
    
    public void put(String name, String path)
    {
        _registry.put(name, path);
    }
    
    public String get(String name)
    {
        return _registry.get(name);
    }
    
    public int size() {
    	return _registry.size();
    }
    
    public Set<String> keySet() {
    	return _registry.keySet();
    }
    
    public String getReplaced(String name)
    {
        String bu = _registry.get(name);
        if (bu != null)
            bu = bu.replace("\\", "\\\\");
        else
            _accessedNull = name;
        return bu;
    }
    
    public String getNullAccessedAndClean()
    {
        String bu = _accessedNull;
        _accessedNull = null;
        return bu;
    }
    
    public void remove(String name)
    {
        _registry.remove(name);
    }
    
    public Map<String, String> getAll()
    {
        return new HashMap<String, String>(_registry);
    }
    
    public void loadFromPreferences()
    {
        Preferences preferences = InstanceScope.INSTANCE.getNode("uk.ac.ncl.safecap.gui.toolpaths");
        Preferences sub2 = preferences.node("list");
        String str = sub2.get("list", "");
        if (str.length() > 0)
        {
            Preferences sub1 = preferences.node("table");
            String[] names = str.split(":");
            for (String name: names)
            {
                if (name.length() > 0)
                {
                    String path = sub1.get(name, "");
                    if (path.length() > 0)
                    {
                        _registry.put(name, path);
                    }
                }
            }
        }
        
        _accessedNull = null;
    }
    
    public void saveToPreferences()
    {
        try
        {
        Preferences preferences = InstanceScope.INSTANCE.getNode("uk.ac.ncl.safecap.gui.toolpaths");
        Preferences sub1 = preferences.node("table");
        sub1.clear();
        StringBuilder sb = new StringBuilder();
        for (String name: _registry.keySet())
        {
            sub1.put(name, _registry.get(name));
            sb.append(name);
            sb.append(":");
        }
        
        Preferences sub2 = preferences.node("list");
        sub2.clear();
        sub2.put("list", sb.toString());
            preferences.flush();
        }
        catch (BackingStoreException e2)
        {
            e2.printStackTrace();
        }
    }
}
