/**
 * Describes STRING tokens
 */



public class StringToken extends Token {

  /**
   * The String value of the token is also stored for STRING tokens
   */
  public String value;
  
  public String toString()
  {
  	//return value.toString();
    return value;
  }

  public Object getValue()
  {
	  return value;
  }
 
  public StringToken(String image)
  {
    this.kind = VNMConstants.STRING  ;
    this.image = image;
    value = image.substring(1,image.length()-1);
  }
}
