import java.util.Stack;

class LLVMGenerator {

    static String header_text = "";
    static String main_text = "";
    static String buffer = "";
    static int reg = 1;
    static int br = 0;
    static Stack<Integer> brstack = new Stack<Integer>();

    static void printInt(String id) {
        buffer += "%" + reg + " = load i32, i32* " + id + "\n";
        reg++;
        buffer += "%" + reg + " = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strp, i32 0, i32 0), i32 %" + (reg - 1) + ")\n";
        reg++;
    }

    static void printReal(String id) {
        buffer += "%" + reg + " = load double, double* " + id + "\n";
        reg++;
        buffer += "%" + reg + " = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpd, i32 0, i32 0), double %" + (reg - 1) + ")\n";
        reg++;
    }

    static void printBool(String id) {
        buffer += "%" + reg + " = load i1, i1* " + id + "\n";
        reg++;
        buffer += "%" + reg + " = zext i1 %" + (reg - 1) + " to i32\n";
        reg++;
        buffer += "%" + reg + " = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpb, i32 0, i32 0), i32 %" + (reg - 1) + ")\n";
        reg++;
    }

    static void readInt(String id) {
        buffer += "%" + reg + " = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @strs, i32 0, i32 0), i32* " + id + ")\n";
        reg++;
    }

    static void readReal(String id) {
        buffer += "%" + reg + " = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strsd, i32 0, i32 0), double* " + id + ")\n";
        reg++;
        buffer += "%" + reg + " = trunc i32 %" + (reg - 1) + " to i1\n";
        reg++;
    }

    static void readBool(String id) {
        buffer += "%" + reg + " = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @strsb, i32 0, i32 0), i1* %" + id + ")\n";
        reg++;
        buffer += "%" + reg + " = trunc i32 %" + (reg - 1) + " to i1\n";
        reg++;
    }

    static void declareInt(String id, Boolean global) {
        if (global) {
            header_text += "@" + id + " = global i32 0\n";
        } else {
            buffer += "%" + id + " = alloca i32\n";
        }
    }

    static void declareReal(String id, Boolean global) {
        if (global) {
            header_text += "@" + id + " = global double 0.0\n";
        } else {
            buffer += "%" + id + " = alloca double\n";
        }
    }

    static void declareBool(String id, Boolean global) {
        if (global) {
            header_text += "@" + id + " = global i1 0\n";
        } else {
            buffer += "%" + id + " = alloca i1\n";
        }
    }

    static void assignInt(String id, String value) {
        buffer += "store i32 " + value + ", i32* " + id + "\n";
    }

    static void assignReal(String id, String value) {
        buffer += "store double " + value + ", double* " + id + "\n";
    }

    static void assignBool(String id, String value) {
        buffer += "store i1 " + value + ", i1* " + id + "\n";
    }

    static void orBool(String val1, String val2) {
        buffer += "%" + reg + " = or i1 " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void andBool(String val1, String val2) {
        buffer += "%" + reg + " = and i1 " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void xorBool(String val1, String val2) {
        buffer += "%" + reg + " = xor i1 " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void notBool(String val) {
        buffer += "%" + reg + " = xor i1 " + val + ", 1\n";
        reg++;
    }

    static void addInt(String val1, String val2) {
        buffer += "%" + reg + " = add i32 " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void addReal(String val1, String val2) {
        buffer += "%" + reg + " = fadd double " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void subInt(String val1, String val2) {
        buffer += "%" + reg + " = sub i32 " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void subReal(String val1, String val2) {
        buffer += "%" + reg + " = fsub double " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void mulInt(String val1, String val2) {
        buffer += "%" + reg + " = mul i32 " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void mulReal(String val1, String val2) {
        buffer += "%" + reg + " = fmul double " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void divInt(String val1, String val2) {
        buffer += "%" + reg + " = sdiv i32 " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void divReal(String val1, String val2) {
        buffer += "%" + reg + " = fdiv double " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static int loadInt(String id) {
        buffer += "%" + reg + " = load i32, i32* " + id + "\n";
        reg++;
        return reg - 1;
    }

    static int loadReal(String id) {
        buffer += "%" + reg + " = load double, double* " + id + "\n";
        reg++;
        return reg - 1;
    }

    static int loadBool(String id) {
        buffer += "%" + reg + " = load i1, i1* " + id + "\n";
        reg++;
        return reg - 1;
    }

    static void icmp_constant(String id, String value, String type, String cond) {
        buffer += "%" + reg + " = load " + type + ", " + type + "* " + id + "\n";
        reg++;
        buffer += "%" + reg + " = icmp " + cond + " " + type + " %" + (reg - 1) + ", " + value + "\n";
        reg++;
    }

    static void icmp_vars(String id1, String id2, String type, String cond) {
        buffer += "%" + reg + " = load " + type + ", " + type + "* " + id1 + "\n";
        reg++;
        buffer += "%" + reg + " = load " + type + ", " + type + "* " + id2 + "\n";
        reg++;
        buffer += "%" + reg + " = icmp " + cond + " " + type + " %" + (reg - 2) + ", %" + (reg - 1) + "\n";
        reg++;
    }

    static void ifstart() {
        br++;
        buffer += "br i1 %" + (reg - 1) + ", label %true" + br + ", label %false" + br + "\n";
        buffer += "true" + br + ":\n";
        brstack.push(br);
    }

    static void ifend() {
        int b = brstack.pop();
        buffer += "br label %end" + b + "\n";
        buffer += "false" + b + ":\n";
        brstack.push(b);
    }

    static void elseend() {
        int b = brstack.pop();
        buffer += "br label %end" + b + "\n";
        buffer += "end" + b + ":\n";
    }

    static void loopstart(String id/*, String value, String cond*/) {
        br++;
        buffer += "br label %cond" + br + "\n";
        buffer += "cond" + br + ":\n";

        int tmp = loadInt(id);
        addInt("%" + Integer.toString(tmp), "1");
        assignInt(id, "%" + Integer.toString(reg - 1));
    }

    static void loopblockstart() {
        buffer += "br i1 %" + (reg - 1) + ", label %true" + br + ", label %false" + br + "\n";
        buffer += "true" + br + ":\n";
        brstack.push(br);
    }

    static void loopend() {
        int b = brstack.pop();
        buffer += "br label %cond" + b + "\n";
        buffer += "false" + b + ":\n";
    }

    static void close_main() {
        main_text += buffer;
    }

    static String generate() {
        String text = "";
        text += "declare i32 @printf(i8*, ...)\n";
        text += "declare i32 @__isoc99_scanf(i8*, ...)\n";
        text += "@strpb = constant [4 x i8] c\"%d\\0A\\00\"\n";
        text += "@strp = constant [4 x i8] c\"%d\\0A\\00\"\n";
        text += "@strpd = constant [4 x i8] c\"%f\\0A\\00\"\n";
        text += "@strs = constant [3 x i8] c\"%d\\00\"\n";
        text += "@strsd = constant [4 x i8] c\"%lf\\00\"\n";
        text += "@strsb = constant [3 x i8] c\"%d\\00\"\n";
        text += header_text;
        text += "define i32 @main() nounwind{\n";
        text += main_text;
        text += "ret i32 0 }\n";
        return text;
    }

}