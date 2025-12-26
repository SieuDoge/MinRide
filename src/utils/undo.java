package utils;

import data_structures.StackNQueue.Stack;

public class undo {
    
    // Command Interface
    public interface Command {
        void undo();
        void redo(); // Optional
    }
    
    private Stack<Command> history;
    
    public undo() {
        history = new Stack<>(50); // Capacity 50
    }
    
    public void addCommand(Command cmd) {
        history.push(cmd);
    }
    
    public void undoLast() {
        if (!history.isEmpty()) {
            Command cmd = history.pop();
            cmd.undo();
            System.out.println("Undid last action.");
        } else {
            System.out.println("Nothing to undo.");
        }
    }
}