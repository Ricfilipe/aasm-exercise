public class MatrixTask {
    public String myName, peerName;
    public Task myTask, peerTask;

    public MatrixTask(String substring) {
        String [] names = substring.split("\\|");
        this.myTask = new Task(names[0]);
        this.peerTask = new Task(names[1]);
        this.myName = myTask.name;
        this.peerName = peerTask.name;
    }
}
