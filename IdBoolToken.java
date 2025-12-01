/**
 * Describes IDBOOL tokens
 */

public class IdBoolToken extends Token {

  public String value;
  
  public String toString()
  {
  	return value.toString();
  }

  public Object getValue()
  {
	return value;
  }
 
  public IdBoolToken(String image)
  {
    this.kind = VNMConstants.IDBOOL  ;
    this.image = image;
   	value = image;
  }

}
