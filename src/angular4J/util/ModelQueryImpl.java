package angular4J.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.Alternative;

import angular4J.util.ModelQuery;

/**
 * ModelQuery implementation
 */
@Alternative
public class ModelQueryImpl implements ModelQuery, Serializable {

   private final Map<String, Object> data = new HashMap<>();

   private Class owner;

   public Map<String, Object> getData() {
      return data;
   }

   @Override
   public ModelQuery setProperty(String model, Object value) {
      data.put(model, value);
      return this;
   }

   @Override
   public ModelQuery pushTo(String objectName, Object value) {

      if (!data.containsKey("zadd")) {
         data.put("zadd", new HashMap<String, Set<Object>>());
      }

      Map<String, Set<Object>> params = (Map<String, Set<Object>>) data.get("zadd");

      if (!params.containsKey(objectName)) {
         params.put(objectName, new HashSet<>());
      }

      params.get(objectName).add(value);

      return this;

   }

   @Override
   public ModelQuery removeFrom(String objectName, Object value) {

      return removeFrom(objectName, value, "NAN");

   }

   @Override
   public ModelQuery removeFrom(String objectName, Object value, String key) {

      Map<String, Set<Object>> params;

      if (key.equals("NAN")) {
         if (!data.containsKey("rm")) {
            data.put("rm", new HashMap<String, Set<Object>>());
         }
         params = (Map<String, Set<Object>>) data.get("rm");
      } else {
         if (!data.containsKey("rm-k")) {
            data.put("rm-k", new HashMap<String, Set<Object>>());
         }
         params = (Map<String, Set<Object>>) data.get("rm-k");

      }

      if (!params.containsKey(objectName)) {
         params.put(objectName, new HashSet<>());
      }

      params.get(objectName).add(value);

      params.get(objectName).add("equalsKey:" + key);

      return this;
   }

   public Class getOwner() {
      return owner;
   }

   public void setOwner(Class owner) {
      this.owner = owner;
   }

   @Override
   public String getTargetServiceClass() {
      return getOwner().getSimpleName();
   }
}
