/**
 * Describes IDVEC tokens
 */

public class IdVecToken extends Token {

  public String value;
  
  public String toString()
  {
  	return value.toString();
  }

  public Object getValue()
  {
	return value;
  }
 
  public IdVecToken(String image)
  {
    this.kind = VNMConstants.IDVEC  ;
    this.image = image;
   	value = image;
  }

}
