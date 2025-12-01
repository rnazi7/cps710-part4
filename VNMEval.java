/* VNMEval.java */
public class VNMEval implements VNMVisitor {

  // helper to make casting easier and code cleaner
  private int getInt(Object obj) {
    return (Integer) obj;
  }

  private boolean getBool(Object obj) {
    return (Boolean) obj;
  }

  // standard visitor stuff required by javaCC
  public Object defaultVisit(SimpleNode node, Object data) throws Exception {
    node.childrenAccept(this, data);
    return data;
  }

  public Object visit(SimpleNode node, Object data) throws Exception {
    return defaultVisit(node, data);
  }

  // --- PRINTING ---

  public Object visit(ASTPrint node, Object data) throws Exception {
    int num = node.jjtGetNumChildren();
    for (int i = 0; i < num; i++) {
      Object result = node.jjtGetChild(i).jjtAccept(this, null);
      if (result != null) {
          System.out.print(result);
      }
    }
    return null;
  }

  public Object visit(ASTPrint_ln node, Object data) throws Exception {
    // reuse the logic above essentially
    int num = node.jjtGetNumChildren();
    for (int i = 0; i < num; i++) {
      Object result = node.jjtGetChild(i).jjtAccept(this, null);
      if (result != null) {
          System.out.print(result);
      }
    }
    System.out.println(""); // new line
    return null;
  }

  // --- MATH OPERATIONS ---

  public Object visit(ASTsum node, Object data) throws Exception {
    // loop through all children to support multiple additions
    int result = 0;
    int num = node.jjtGetNumChildren();
    
    for (int i = 0; i < num; i++) {
       Object childVal = node.jjtGetChild(i).jjtAccept(this, data);
       result += getInt(childVal);
    }
    return result;
  }

  public Object visit(ASTmul node, Object data) throws Exception {
    // just standard binary multiplication
    int left = getInt(node.jjtGetChild(0).jjtAccept(this, data));
    int right = getInt(node.jjtGetChild(1).jjtAccept(this, data));
    return left * right;
  }

  public Object visit(ASTdiv node, Object data) throws Exception {
    int left = getInt(node.jjtGetChild(0).jjtAccept(this, data));
    int right = getInt(node.jjtGetChild(1).jjtAccept(this, data));
    return left / right;
  }

  public Object visit(ASTmod node, Object data) throws Exception {
    int left = getInt(node.jjtGetChild(0).jjtAccept(this, data));
    int right = getInt(node.jjtGetChild(1).jjtAccept(this, data));
    return left % right;
  }

  public Object visit(ASTneg node, Object data) throws Exception {
    // flip the sign
    int val = getInt(node.jjtGetChild(0).jjtAccept(this, data));
    return -val;
  }

  public Object visit(ASTpos node, Object data) throws Exception {
    return node.jjtGetChild(0).jjtAccept(this, data);
  }

  // --- LOGIC GATES ---

  public Object visit(ASTor node, Object data) throws Exception {
    int num = node.jjtGetNumChildren();
    for (int i = 0; i < num; i++) {
       boolean val = getBool(node.jjtGetChild(i).jjtAccept(this, data));
       if (val) return true;
    }
    return false;
  }

  public Object visit(ASTand node, Object data) throws Exception {
    int num = node.jjtGetNumChildren();
    for (int i = 0; i < num; i++) {
       boolean val = getBool(node.jjtGetChild(i).jjtAccept(this, data));
       if (!val) return false;
    }
    return true;
  }

  public Object visit(ASTnot node, Object data) throws Exception {
    boolean val = getBool(node.jjtGetChild(0).jjtAccept(this, data));
    return !val;
  }

  // --- CONTROL FLOW ---

  public Object visit(ASTIf node, Object data) throws Exception {
    // first child is always condition
    boolean condition = getBool(node.jjtGetChild(0).jjtAccept(this, data));
    
    if (condition) {
       return node.jjtGetChild(1).jjtAccept(this, data);
    }
    
    // check if we have an else block
    if (node.jjtGetNumChildren() > 2) {
       Node elseBlock = node.jjtGetChild(2);
       // ignore null nodes
       if (!(elseBlock instanceof ASTNULL)) {
           return elseBlock.jjtAccept(this, data);
       }
    }
    return null;
  }

  public Object visit(ASTNULL node, Object data) throws Exception {
    return null;
  }

  // --- COMPARISONS ---

  public Object visit(ASTcomparison node, Object data) throws Exception {
    int left = getInt(node.jjtGetChild(0).jjtAccept(this, data));
    int right = getInt(node.jjtGetChild(2).jjtAccept(this, data));
    Node op = node.jjtGetChild(1);

    // check which operator it is
    if (op instanceof ASTle) return left < right;
    if (op instanceof ASTleq) return left <= right;
    if (op instanceof ASTgre) return left > right;
    if (op instanceof ASTgeq) return left >= right;
    if (op instanceof ASTeq) return left == right;
    if (op instanceof ASTneq) return left != right;
    
    return false;
  }

  // these are handled in the comparison visitor above
  public Object visit(ASTle n, Object d) throws Exception { return null; }
  public Object visit(ASTleq n, Object d) throws Exception { return null; }
  public Object visit(ASTgre n, Object d) throws Exception { return null; }
  public Object visit(ASTgeq n, Object d) throws Exception { return null; }
  public Object visit(ASTeq n, Object d) throws Exception { return null; }
  public Object visit(ASTneq n, Object d) throws Exception { return null; }

  // --- LITERALS ---

  public Object visit(ASTnumber node, Object data) throws Exception {
    Object val = node.jjtGetValue();
    // handle both cases since token might already be int
    if (val instanceof Integer) return val;
    return Integer.parseInt(val.toString());
  }

  public Object visit(ASTstring node, Object data) throws Exception {
    String raw = node.jjtGetValue().toString();
    // remove quotes
    if (raw.length() >= 2 && raw.startsWith("\"")) {
        return raw.substring(1, raw.length() - 1);
    }
    return raw;
  }

  public Object visit(ASTTRUE node, Object data) throws Exception { return true; }
  public Object visit(ASTFALSE node, Object data) throws Exception { return false; }

  // --- IGNORED NODES (just pass through) ---
  
  public Object visit(ASTbody n, Object d) throws Exception { return defaultVisit(n, d); }
  public Object visit(ASTclause n, Object d) throws Exception { return defaultVisit(n, d); }
  public Object visit(ASTvar_decl n, Object d) throws Exception { return defaultVisit(n, d); }
  public Object visit(ASTfn_decl n, Object d) throws Exception { return defaultVisit(n, d); }
  public Object visit(ASTident_list n, Object d) throws Exception { return defaultVisit(n, d); }
  public Object visit(ASTfn_call n, Object d) throws Exception { return defaultVisit(n, d); }
  public Object visit(ASTboolean_call n, Object d) throws Exception { return defaultVisit(n, d); }
  public Object visit(ASTexp_list n, Object d) throws Exception { return defaultVisit(n, d); }
  public Object visit(ASTcondition_list n, Object d) throws Exception { return defaultVisit(n, d); }
  public Object visit(ASTReturn n, Object d) throws Exception { return defaultVisit(n, d); }
  public Object visit(ASTAssign n, Object d) throws Exception { return defaultVisit(n, d); }
  public Object visit(ASTFor n, Object d) throws Exception { return defaultVisit(n, d); }
  public Object visit(ASTWhile n, Object d) throws Exception { return defaultVisit(n, d); }
  public Object visit(ASTvec_const n, Object d) throws Exception { return defaultVisit(n, d); }
  public Object visit(ASTidvec n, Object d) throws Exception { return defaultVisit(n, d); }
  public Object visit(ASTidnum n, Object d) throws Exception { return defaultVisit(n, d); }
  public Object visit(ASTidbool n, Object d) throws Exception { return defaultVisit(n, d); }
  public Object visit(ASTin n, Object d) throws Exception { return defaultVisit(n, d); }
  public Object visit(ASTnotin n, Object d) throws Exception { return defaultVisit(n, d); }

}


/* JavaCC - OriginalChecksum=22d58c5389ead436110ed9c53d7f4358 (do not edit this line) */
