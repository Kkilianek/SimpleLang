import java.util.*;

class Value {
    public String type;
    public String value;

    public Value(String type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Value{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

public class LLVMActions extends SimpleLangBaseListener {

    HashMap<String, String> globalVariables = new HashMap<>();
    HashMap<String, String> variables = new HashMap<>();
    HashSet<String> types = new HashSet<>() {{
        add("int");
        add("real");
        add("bool");
    }};
    List<Value> argumentsList = new ArrayList<>();
    Stack<Value> stack = new Stack<>();
    Boolean global;

    @Override
    public void enterProg(SimpleLangParser.ProgContext ctx) {
        global = true;
    }

    @Override
    public void exitProg(SimpleLangParser.ProgContext ctx) {
        LLVMGenerator.close_main();
        System.out.println(LLVMGenerator.generate());
    }

    @Override
    public void exitAnd(SimpleLangParser.AndContext ctx) {
        Value v1 = stack.pop();
        Value v2 = stack.pop();
        if (v1.type.equals(v2.type)) {
            if (v1.type.equals("bool")) {
                LLVMGenerator.andBool(v2.value, v1.value);
                stack.push(new Value("bool", "%" + (LLVMGenerator.reg - 1)));
            }
        } else {
            error(ctx.getStart().getLine(), "and type mismatch, needed bool");
        }
    }

    @Override
    public void exitOr(SimpleLangParser.OrContext ctx) {
        Value v1 = stack.pop();
        Value v2 = stack.pop();
        if (v1.type.equals(v2.type)) {
            if (v1.type.equals("bool")) {
                LLVMGenerator.orBool(v2.value, v1.value);
                stack.push(new Value("bool", "%" + (LLVMGenerator.reg - 1)));
            }
        } else {
            error(ctx.getStart().getLine(), "or type mismatch, needed bool");
        }
    }

    @Override
    public void exitNot(SimpleLangParser.NotContext ctx) {
        Value v1 = stack.pop();
        if (v1.type.equals("bool")) {
            LLVMGenerator.notBool(v1.value);
            stack.push(new Value("bool", "%" + (LLVMGenerator.reg - 1)));
        } else {
            error(ctx.getStart().getLine(), "not type mismatch, needed bool");
        }
    }

    @Override
    public void exitXor(SimpleLangParser.XorContext ctx) {
        Value v1 = stack.pop();
        Value v2 = stack.pop();
        if (v1.type.equals(v2.type)) {
            if (v1.type.equals("bool")) {
                LLVMGenerator.xorBool(v2.value, v1.value);
                stack.push(new Value("bool", "%" + (LLVMGenerator.reg - 1)));
            }
        } else {
            error(ctx.getStart().getLine(), "xor type mismatch, needed bool");
        }
    }

    @Override
    public void exitAssignment(SimpleLangParser.AssignmentContext ctx) {
        String ID;
        try {
            ID = ctx.ID().getText();
        } catch (NullPointerException e) {
            ID = ctx.declaration().getChild(1).getText();
        }
        if (!variables.containsKey(ID) && !globalVariables.containsKey(ID)) {
            error(ctx.getStart().getLine(), "variable not declared");
        }
        Value v = stack.pop();
        if (variables.containsKey(ID)) {
            if (!v.type.equals(variables.get(ID))) {
                error(ctx.getStart().getLine(), "assignment type mismatch");
            }
        } else {
            if (!v.type.equals(globalVariables.get(ID))) {
                error(ctx.getStart().getLine(), "assignment type mismatch");
            }
        }
        if (v.type.equals("int")) {
            LLVMGenerator.assignInt(resolveScope(ID), v.value);
        }
        if (v.type.equals("real")) {
            LLVMGenerator.assignReal(resolveScope(ID), v.value);
        }
        if (v.type.equals("bool")) {
            LLVMGenerator.assignBool(resolveScope(ID), v.value);
        }
    }

    @Override
    public void exitDeclaration(SimpleLangParser.DeclarationContext ctx) {
        String ID = ctx.ID().getText();
        String TYPE = ctx.type().getText();
        if ((!variables.containsKey(ID) && !global) || (!globalVariables.containsKey(ID) && global)) {
            if (types.contains(TYPE)) {
                if (global) {
                    globalVariables.put(ID, TYPE);
                } else {
                    variables.put(ID, TYPE);
                }
                if (TYPE.equals("int")) {
                    LLVMGenerator.declareInt(ID, global);
                } else if (TYPE.equals("real")) {
                    LLVMGenerator.declareReal(ID, global);
                } else if (TYPE.equals("bool")) {
                    LLVMGenerator.declareBool(ID, global);
                }
            } else {
                error(ctx.getStart().getLine(), ", unknown variable type: " + TYPE);
            }
        } else {
            error(ctx.getStart().getLine(), ", variable already defined: " + ID);
        }
    }

    @Override
    public void exitCall_function(SimpleLangParser.Call_functionContext ctx) {
        String FUNC_NAME = ctx.function_name().getText();
        if (FUNC_NAME.equals("print")) {
            if (argumentsList.size() == 1) {
                Value argument = argumentsList.get(0);
                String ID = argument.value;
                String type = variables.get(ID);
                if (type == null) {
                    type = globalVariables.get(ID);
                }
                if (type != null) {
                    if (type.equals("int")) {
                        LLVMGenerator.printInt(resolveScope(ID));
                    } else if (type.equals("real")) {
                        LLVMGenerator.printReal(resolveScope(ID));
                    } else if (type.equals("bool")) {
                        LLVMGenerator.printBool(resolveScope(ID));
                    }
                } else {
                    error(ctx.getStart().getLine(), ", unknown variable: " + ID);
                }
            } else {
                error(ctx.getStart().getLine(), ", to many arguments in function print. Expected 1, Got: " + argumentsList.size());
            }
        } else if (FUNC_NAME.equals("read")) {
            if (argumentsList.size() == 1) {
                Value argument = argumentsList.get(0);
                String ID = argument.value;
                String type = variables.get(ID);
                if (type == null) {
                    type = globalVariables.get(ID);
                }
                if (type != null) {
                    if (type.equals("int")) {
                        LLVMGenerator.readInt(resolveScope(ID));
                    } else if (type.equals("real")) {
                        LLVMGenerator.readReal(resolveScope(ID));
                    } else if (type.equals("bool")) {
                        LLVMGenerator.readBool(resolveScope(ID));
                    }
                } else {
                    error(ctx.getStart().getLine(), ", unknown variable: " + ID);
                }
            } else {
                error(ctx.getStart().getLine(), ", to many arguments in function read. Expected 1, Got: " + argumentsList.size());
            }
        }
        argumentsList.clear();
    }

    @Override
    public void exitValue(SimpleLangParser.ValueContext ctx) {
        try {
            argumentsList.add(new Value("ID", ctx.ID().getText()));
        } catch (NullPointerException ignored) {
        }
        try {
            argumentsList.add(new Value("int", ctx.INT().getText()));

        } catch (NullPointerException ignored) {
        }
        try {
            argumentsList.add(new Value("real", ctx.REAL().getText()));

        } catch (NullPointerException ignored) {
        }
        try {
            argumentsList.add(new Value("bool", ctx.BOOL().getText()));
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public void exitInt(SimpleLangParser.IntContext ctx) {
        stack.push(new Value("int", ctx.INT().getText()));
    }

    @Override
    public void exitReal(SimpleLangParser.RealContext ctx) {
        stack.push(new Value("real", ctx.REAL().getText()));
    }

    @Override
    public void exitBool(SimpleLangParser.BoolContext ctx) {
        stack.push(new Value("bool", ctx.BOOL().getText()));
    }

    @Override
    public void exitId(SimpleLangParser.IdContext ctx) {
        String ID = ctx.ID().getText();
        if (variables.containsKey(ID) || globalVariables.containsKey(ID)) {
            String type = variables.get(ID);
            if (type == null) {
                type = globalVariables.get(ID);
            }
            int reg = -1;
            if (type.equals("int")) {
                reg = LLVMGenerator.loadInt(resolveScope(ID));
            } else if (type.equals("real")) {
                reg = LLVMGenerator.loadReal(resolveScope(ID));
            } else if (type.equals("bool")) {
                reg = LLVMGenerator.loadBool(resolveScope(ID));
            }
            stack.push(new Value(type, "%" + reg));
        } else {
            error(ctx.getStart().getLine(), "no such variable");
        }
    }

    @Override
    public void exitAdd(SimpleLangParser.AddContext ctx) {
        Value v1 = stack.pop();
        Value v2 = stack.pop();
        if (v1.type.equals(v2.type)) {
            if (v1.type.equals("int")) {
                LLVMGenerator.addInt(v1.value, v2.value);
                stack.push(new Value("int", "%" + (LLVMGenerator.reg - 1)));
            }
            if (v1.type.equals("real")) {
                LLVMGenerator.addReal(v1.value, v2.value);
                stack.push(new Value("real", "%" + (LLVMGenerator.reg - 1)));
            }
        } else {
            error(ctx.getStart().getLine(), "add type mismatch");
        }
    }

    @Override
    public void exitMul(SimpleLangParser.MulContext ctx) {
        Value v1 = stack.pop();
        Value v2 = stack.pop();
        if (v1.type.equals(v2.type)) {
            if (v1.type.equals("int")) {
                LLVMGenerator.mulInt(v1.value, v2.value);
                stack.push(new Value("int", "%" + (LLVMGenerator.reg - 1)));
            }
            if (v1.type.equals("real")) {
                LLVMGenerator.mulReal(v1.value, v2.value);
                stack.push(new Value("real", "%" + (LLVMGenerator.reg - 1)));
            }
        } else {
            error(ctx.getStart().getLine(), "multiplication type mismatch");
        }
    }

    @Override
    public void exitSub(SimpleLangParser.SubContext ctx) {
        Value v1 = stack.pop();
        Value v2 = stack.pop();
        if (v1.type.equals(v2.type)) {
            if (v1.type.equals("int")) {
                LLVMGenerator.subInt(v2.value, v1.value);
                stack.push(new Value("int", "%" + (LLVMGenerator.reg - 1)));
            }
            if (v1.type.equals("real")) {
                LLVMGenerator.subReal(v2.value, v1.value);
                stack.push(new Value("real", "%" + (LLVMGenerator.reg - 1)));
            }
        } else {
            error(ctx.getStart().getLine(), "subtraction type mismatch");
        }
    }

    @Override
    public void exitDiv(SimpleLangParser.DivContext ctx) {
        Value v1 = stack.pop();
        Value v2 = stack.pop();
        if (v1.type.equals(v2.type)) {
            if (v1.type.equals("int")) {
                LLVMGenerator.divInt(v2.value, v1.value);
                stack.push(new Value("int", "%" + (LLVMGenerator.reg - 1)));
            }
            if (v1.type.equals("real")) {
                LLVMGenerator.divReal(v2.value, v1.value);
                stack.push(new Value("real", "%" + (LLVMGenerator.reg - 1)));
            }
        } else {
            error(ctx.getStart().getLine(), "division type mismatch");
        }
    }

    @Override
    public void enterBlockif(SimpleLangParser.BlockifContext ctx) {
        LLVMGenerator.ifstart();
    }

    @Override
    public void exitBlockif(SimpleLangParser.BlockifContext ctx) {
        LLVMGenerator.ifend();
    }

    @Override
    public void exitBlockelse(SimpleLangParser.BlockelseContext ctx) {
        LLVMGenerator.elseend();
    }

    @Override
    public void exitCondition(SimpleLangParser.ConditionContext ctx) {
        String ID = ctx.ID().getText();
        String operation;
        try {
            operation = ctx.if_operation().getText();
        } catch (NullPointerException e) {
            operation = "";
        }
        if (!operation.equals("")) {
            String value = ctx.comparable_value().getText();
            if (value.matches("^[a-zA-Z]+$")) {
                if ((globalVariables.containsKey(ID) || variables.containsKey(ID)) && (globalVariables.containsKey(value) || variables.containsKey(value))) {
                    String type1 = "";
                    if (globalVariables.containsKey(ID)) {
                        type1 = globalVariables.get(ID);
                    } else if (variables.containsKey(ID)) {
                        type1 = variables.get(ID);
                    }
                    String type2 = "";
                    if (globalVariables.containsKey(value)) {
                        type2 = globalVariables.get(value);
                    } else if (variables.containsKey(value)) {
                        type2 = variables.get(value);
                    }
                    if (type1.equals(type2)) {
                        String operation_text = "";
                        switch (operation) {
                            case "==":
                                operation_text = "eq";
                                break;
                            case "!=":
                                operation_text = "ne";
                            break;
                        case "<":
                            operation_text = "slt";
                            break;
                        case ">":
                            operation_text = "sgt";
                            break;
                        case ">=":
                            operation_text = "sge";
                            break;
                        case "<=":
                            operation_text = "sle";
                            break;
                        default:
                            operation_text = "error";
                            break;
                    }
                    if (operation_text.equals("error")) {
                        error(ctx.getStart().getLine(), "unsupported operation");
                    }
                    if (type1.equals("int")) {
                        LLVMGenerator.icmp_vars(resolveScope(ID), resolveScope(value), "i32", operation_text);
                        stack.push(new Value("bool", "%" + (LLVMGenerator.reg - 1)));
                    } else if (type1.equals("real")) {
                        LLVMGenerator.icmp_vars(resolveScope(ID), resolveScope(value), "double", operation_text);
                        stack.push(new Value("bool", "%" + (LLVMGenerator.reg - 1)));
                    } else {
                        error(ctx.getStart().getLine(), "unsupported type");
                    }
                } else {
                    error(ctx.getStart().getLine(), "variables have different types");
                }
            } else {
                error(ctx.getStart().getLine(), "variable not defined");
            }

        } else {
                if (globalVariables.containsKey(ID) || variables.containsKey(ID)) {
                    String type = "";
                    if (globalVariables.containsKey(ID)) {
                        type = globalVariables.get(ID);
                    } else if (variables.containsKey(ID)) {
                        type = variables.get(ID);
                    }
                    if ((type.equals("int") && value.contains("\\.")) || (type.equals("real") && !value.contains("\\."))) {
                        error(ctx.getStart().getLine(), "wrong type comparison");
                    }
                    String operation_text = "";
                    switch (operation) {
                        case "==":
                            operation_text = "eq";
                            break;
                    case "!=":
                        operation_text = "ne";
                        break;
                    case "<":
                        operation_text = "ult";
                        break;
                    case ">":
                        operation_text = "ugt";
                        break;
                    case ">=":
                        operation_text = "uge";
                        break;
                    case "<=":
                        operation_text = "ule";
                        break;
                    default:
                        operation_text = "error";
                        break;
                }
                if (operation_text.equals("error")) {
                    error(ctx.getStart().getLine(), "unsupported operation");
                }
                if (type.equals("int")) {
                    LLVMGenerator.icmp_constant(resolveScope(ID), value, "i32", operation_text);
                    stack.push(new Value("bool", "%" + (LLVMGenerator.reg - 1)));
                } else if (type.equals("real")) {
                    LLVMGenerator.icmp_constant(resolveScope(ID), value, "double", operation_text);
                    stack.push(new Value("bool", "%" + (LLVMGenerator.reg - 1)));
                } else {
                    error(ctx.getStart().getLine(), "unsupported type");
                }
                } else {
                    error(ctx.getStart().getLine(), "variable not defined");
                }
            }
        } else {
            if (globalVariables.containsKey(ID) || variables.containsKey(ID)) {
                String type = "";
                if (globalVariables.containsKey(ID)) {
                    type = globalVariables.get(ID);
                } else if (variables.containsKey(ID)) {
                    type = variables.get(ID);
                }
                if (type.equals("bool")) {
                    LLVMGenerator.icmp_constant(resolveScope(ID), "1", "i1", "eq");
                    stack.push(new Value("bool", "%" + (LLVMGenerator.reg - 1)));
                } else {
                    error(ctx.getStart().getLine(), "unsupported type");
                }
            } else {
                error(ctx.getStart().getLine(), "variable not defined");
            }
        }
    }

    @Override
    public void enterLoopblock(SimpleLangParser.LoopblockContext ctx) {
        String ID = ctx.condition().getChild(0).getText();
        LLVMGenerator.loopstart(resolveScope(ID));
    }

    @Override
    public void enterBlockfor(SimpleLangParser.BlockforContext ctx) {
        LLVMGenerator.loopblockstart();
    }

    @Override
    public void exitBlockfor(SimpleLangParser.BlockforContext ctx) {
        LLVMGenerator.loopend();
    }

    public String resolveScope(String ID) {
        String id;
        if (global) {
            id = "@" + ID;
        } else {
            if (!variables.containsKey(ID)) {
                id = "@" + ID;
            } else {
                id = "%" + ID;
            }
        }
        return id;
    }


    void error(int line, String msg) {
        System.err.println("Error, line " + line + ", " + msg);
        System.exit(1);
    }

}