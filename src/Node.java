
public class Node<E> {
	private E data;   //数据域
    private Node<E> left;  //左孩子
    private Node<E> right;  //右孩子
    
    Node(){}
    
    Node(E e){
        this.data = e;
    }
    
    Node(E data, Node<E> left, Node<E> right){
        this.data = data;
        this.left = left;
        this.right = right;
    }
    
    public void setData(E data){
        this.data = data;
    }
    
    public E getData(){
        return this.data;
    }
    
    public void setLeft(Node<E> left){
        this.left = left;
    }
    
    public Node<E> getLeft(){
        return this.left;
    }
    
    public void setRight(Node<E> right){
        this.right = right;
    }
    
    public Node<E> getRight(){
        return this.right;
    }
}
