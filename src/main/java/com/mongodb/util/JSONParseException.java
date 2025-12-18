package com.mongodb.util;

/**
 * Shim class for the missing com.mongodb.util.JSONParseException.
 */
public class JSONParseException extends RuntimeException {

  private static final long serialVersionUID = -4415279469780082174L;

  final String s;
  final int pos;

  public JSONParseException( String s, int pos ) {
    super( s + " @ " + pos );
    this.s = s;
    this.pos = pos;
  }

  public JSONParseException( String s, int pos, Throwable cause ) {
    super( cause );
    this.s = s;
    this.pos = pos;
  }
  
  @Override
  public String getMessage() {
    return s + " @ " + pos;
  } 
}