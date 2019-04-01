import java.util.ArrayList;
import java.util.List;

public class Interpreter {
 public InterpreterState state;
 public int operation=1;
 private Command lastNormalCommand;
 private Command currentCommand;
 private int currentOP=0;
 private int maxOP=1;

 public String interpret(String arg) {
     String[] args = arg.split(" ");

    if(this.maxOP>1){
        try {
            alternativeOperation(args);
            this.currentCommand = this.lastNormalCommand;
        } catch (Exception e) {
            this.currentCommand = new CmdError("Erro de sintaxe");
            this.maxOP = 1;
            this.operation++;
        }
    }else {

        //fase 1 Selecionar Comando
        getCommand(args[0]);
        if (!currentCommand.checkNum(args.length)) {
            this.currentCommand = new CmdError("Numero errado de argumentos");
        }


        //fase 2 Intepretar estado
        if (!currentCommand.isSpecialCommand()) {
            try {
                getState(args[1]);
            } catch (Exception e) {
                this.currentCommand = new CmdError("Sintaxe errada");
            }
        }


        //fase 3 obter numero de ops
        if (args.length == 3 && !currentCommand.isSpecialCommand()) {
            getOP(args[2]);
        }



        }
    if (!currentCommand.isSpecialCommand()) {
        this.currentOP++;
        this.lastNormalCommand = currentCommand;
        if (this.currentOP >= this.maxOP) {
            this.operation++;
            this.maxOP = 1;
            this.currentOP = 0;
        }
    }


     try {
         return currentCommand.execute(state);
     } catch (Exception e) {
        // return new CmdError("Erro de Syntaxe").execute(state);
         throw new RuntimeException();
     }
 }

    private void alternativeOperation(String[] args) throws Exception {
        if(args.length!=1){
            this.currentCommand = new CmdError("Numero de argumentos e errado");
        }else{
            if (args[0].equals("exit")) {
                this.lastNormalCommand = new CmdExit();
                return;
            }
            String[] arg= args[0].replace("(","").replace(")","").split(",");

            state.findOrCreateOption(arg[0],arg[1]);
        }

    }

    private void getOP(String arg) {
        try{
            this.maxOP = Integer.valueOf(arg);
        }catch(Exception x){
            this.currentCommand = new CmdError("O ultimo argumento nao e um inteiro");
            throw new RuntimeException();
        }

    }

    private void getState(String arg) throws Exception {
        if(currentCommand.isForSingle()){
            buildSingleState(arg);
        }else{
            buildMultiState(arg);
        }

    }

    private void buildMultiState(String arg) {
     //TODO
    }

    private void buildSingleState(String arg) throws Exception {
        int depth=0;
        List<String> tasks = splitTask(arg);
        this.state = new SingleState();
        ((SingleState) this.state).generateState(tasks);
     
    }

    private List<String> splitTask(String arg) {
     int helper =1;
     List<String> buffer= new ArrayList<>();

     for(int i=0; i<arg.length();i++){
        if(i!=0 ){
            if((arg.charAt(i)==',' && arg.charAt(i-1)==']' )|| i==(arg.length()-1) ){

                buffer.add(arg.substring(helper,i));
                helper=i+1;
            }
        }
     }
     return buffer;
    }


    private void getCommand(String arg) {
        if(!arg.startsWith("decide-")){

            if(arg.equals("exit")){
                this.currentCommand = new CmdExit();
            }else {
                this.currentCommand = new CmdError(arg + " nao existe...");
            }
            return;
        }
        String cmd = arg.split("-")[1];
        cmd = cmd.substring(0, 1).toUpperCase() + cmd.substring(1);

        try {
            this.currentCommand = (Command) Class.forName("Command" + cmd).newInstance();
        }catch( ClassNotFoundException e){
            this.currentCommand = new CmdError(arg + " nao existe");
        }catch (Exception x){
            this.currentCommand = new CmdError("Algo correu mal :(");

        }
    }
}
