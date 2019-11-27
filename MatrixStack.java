import java.util.LinkedList;

public class MatrixStack<T>{

    private LinkedList<T> stack = new LinkedList<T>();

    public void push(T element){
        stack.addFirst(element);
    }
    public T top(){
        return stack.getFirst();
    }
    public T pop(){
        return stack.removeFirst();
    }
    public boolean empty(){
        return stack.isEmpty();
    }
}