package com.mongodb.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.bson.json.JsonParseException;

/**
 * Shim class to provide compatibility with legacy code expecting com.mongodb.util.JSON.
 * This class delegates to the new MongoDB Driver 5.x APIs.
 */
public class JSON {

  /**
   * Parses a JSON string and returns a corresponding Java object.
   * Uses BasicDBObject.parse() which is the modern equivalent.
   *
   * @param jsonString the string to parse
   * @return the parsed object (BasicDBObject or BasicDBList)
   */
  public static Object parse( String jsonString ) {
    try {
      if ( jsonString == null ) {
        return null;
      }
      String trimmed = jsonString.trim();
      if ( trimmed.startsWith( "[" ) ) {
        // BasicDBObject.parse expects a document, so if it's an array, we wrap it
        // { "wrapper": [...] }
        BasicDBObject wrapper = BasicDBObject.parse( "{ \"wrapper\": " + jsonString + "}" );
        return wrapper.get( "wrapper" );
      }
      return BasicDBObject.parse( jsonString );
    } catch ( JsonParseException e ) {
      // Map new exception to old exception for compatibility
      throw new com.mongodb.util.JSONParseException( e.getMessage(), 0, e );
    }
  }

  /**
   * Serializes an object into its JSON string representation.
   *
   * @param object the object to serialize
   * @return the JSON string
   */
  public static String serialize( Object object ) {
    if ( object == null ) {
      return "null";
    }
    return object.toString();
  }
}