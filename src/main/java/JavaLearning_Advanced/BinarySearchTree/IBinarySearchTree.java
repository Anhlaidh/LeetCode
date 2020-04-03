package JavaLearning_Advanced.BinarySearchTree;

public interface IBinarySearchTree<E> {

    int size(); //元素的数量

    boolean isEmpty(); //是否为空

    void clear(); //清空所有元素

    void add(E element); //添加元素

    void remove(E element); //删除元素

    boolean contains(E element);//是否包含元素


}