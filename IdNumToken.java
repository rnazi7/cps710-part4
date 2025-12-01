/**
 * Describes IDNUM tokens
 */

public class IdNumToken extends Token {

  public String value;
  
  public String toString()
  {
  	return value.toString();
  }

  public Object getValue()
  {
	return value;
  }
 
  public IdNumToken(String image)
  {
    this.kind = VNMConstants.IDNUM  ;
    this.image = image;
   	value = image;
  }

}
