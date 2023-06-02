class LLVMGenerator {

    static String header_text = "";
    static String main_text = "";
    static int reg = 1;

    static void printInt(String id) {
        main_text += "%" + reg + " = load i32, i32* %" + id + "\n";
        reg++;
        main_text += "%" + reg + " = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strp, i32 0, i32 0), i32 %" + (reg - 1) + ")\n";
        reg++;
    }

    static void printReal(String id) {
        main_text += "%" + reg + " = load double, double* %" + id + "\n";
        reg++;
        main_text += "%" + reg + " = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpd, i32 0, i32 0), double %" + (reg - 1) + ")\n";
        reg++;
    }

    static void printBool(String id) {
        main_text += "%" + reg + " = load i1, i1* %" + id + "\n";
        reg++;
        main_text += "%" + reg + " = zext i1 %" + (reg - 1) + " to i32\n";
        reg++;
        main_text += "%" + reg + " = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strpb, i32 0, i32 0), i32 %" + (reg - 1) + ")\n";
        reg++;
    }

    static void readInt(String id) {
        main_text += "%" + reg + " = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @strs, i32 0, i32 0), i32* %" + id + ")\n";
        reg++;
    }

    static void readReal(String id) {
        main_text += "%" + reg + " = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @strsd, i32 0, i32 0), double* %" + id + ")\n";
        reg++;
    }

    static void readBool(String id) {
        main_text += "%" + reg + " = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @strsb, i32 0, i32 0), i1* %" + id + ")\n";
        reg++;
        main_text += "%" + reg + " = trunc i32 %" + (reg - 1) + " to i1\n";
        reg++;
    }

    static void declareInt(String id) {
        main_text += "%" + id + " = alloca i32\n";
    }

    static void declareReal(String id) {
        main_text += "%" + id + " = alloca double\n";
    }

    static void declareBool(String id) {
        main_text += "%" + id + " = alloca i1\n";
    }

    static void assignInt(String id, String value) {
        main_text += "store i32 " + value + ", i32* %" + id + "\n";
    }

    static void assignReal(String id, String value) {
        main_text += "store double " + value + ", double* %" + id + "\n";
    }

    static void assignBool(String id, String value) {
        main_text += "store i1 " + value + ", i1* %" + id + "\n";
    }

    static void addInt(String val1, String val2) {
        main_text += "%" + reg + " = add i32 " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void addReal(String val1, String val2) {
        main_text += "%" + reg + " = fadd double " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void orBool(String val1, String val2) {
        main_text += "%" + reg + " = or i1 " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void andBool(String val1, String val2) {
        main_text += "%" + reg + " = and i1 " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void xorBool(String val1, String val2) {
        main_text += "%" + reg + " = xor i1 " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void notBool(String val) {
        main_text += "%" + reg + " = xor i1 " + val + ", 1\n";
        reg++;
    }

    static void subInt(String val1, String val2) {
        main_text += "%" + reg + " = sub i32 " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void subReal(String val1, String val2) {
        main_text += "%" + reg + " = fsub double " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void mulInt(String val1, String val2) {
        main_text += "%" + reg + " = mul i32 " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void mulReal(String val1, String val2) {
        main_text += "%" + reg + " = fmul double " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void divInt(String val1, String val2) {
        main_text += "%" + reg + " = sdiv i32 " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static void divReal(String val1, String val2) {
        main_text += "%" + reg + " = fdiv double " + val1 + ", " + val2 + "\n";
        reg++;
    }

    static int loadInt(String id) {
        main_text += "%" + reg + " = load i32, i32* %" + id + "\n";
        reg++;
        return reg - 1;
    }

    static int loadReal(String id) {
        main_text += "%" + reg + " = load double, double* %" + id + "\n";
        reg++;
        return reg - 1;
    }

    static int loadBool(String id) {
        main_text += "%" + reg + " = load i1, i1* %" + id + "\n";
        reg++;
        return reg - 1;
    }

    static void icmp_constant(String id, String value, String type, String cond) {
        main_text += "%" + reg + " = load " + type + ", " + type + "* %" + id + "\n";
        reg++;
        main_text += "%" + reg + " = icmp " + cond + " " + type + " %" + (reg - 1) + ", " + value + "\n";
        reg++;
    }

    static void icmp_vars(String id1, String id2, String type, String cond) {
        main_text += "%" + reg + " = load " + type + ", " + type + "* %" + id1 + "\n";
        reg++;
        main_text += "%" + reg + " = load " + type + ", " + type + "* %" + id2 + "\n";
        reg++;
        main_text += "%" + reg + " = icmp " + cond + " " + type + " %" + (reg - 2) + ", %" + (reg - 1) + "\n";
        reg++;
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