public class BSTSet{
    private TNode root;
    
    /* CONSTRUCTORS */
    public BSTSet(){
        root = null;
    }
    
    public BSTSet(int[] input){
        for(int i = 0; i < input.length; i++){
            add(input[i]); //Add each element from the array into the tree
        }
    }
    
    /**************************************************/
    /* ISIN METHOD */
    
    public boolean isIn(int v){
        return isIn(root, v);
    }
    private boolean isIn(TNode node, int v){
        if (node == null){
            return false;
        }
        
        //Checking if element exists in tree
        if(node.element == v){
            return true;
        }
        
        //Traversing based on comparisons
        if(v < node.element){
            return isIn(node.left, v);
        }else{
            return isIn(node.right, v);
        }
        
    }
    
    /**************************************************/
    /* ADD METHOD */
    
    public void add(int v){
        root = add(root, v);
    }
    private TNode add(TNode node, int v){
        //Add node with given value
        if (node == null){
            return new TNode(v, null, null);
        }
        
        //No change if value exists in tree already
        if(node.element == v){
            return node;
        }
        
        //Traversing based on comparisons
        if(v < node.element){
            node.left = add(node.left, v);
        }else{
            node.right = add(node.right, v);
        }
        
        return node;
    }
    
    /**************************************************/
    /* REMOVE METHOD */
    
    public boolean remove(int v){
        if(isIn(v) == false){ 
            return false; 
        }else{
            root = remove(root, v);
            return true;
        }
    }
    private TNode remove(TNode node, int v){
        //Traversing based on comparisons
        if(v < node.element){
            node.left  = remove(node.left, v);
        }else if(v > node.element){
            node.right = remove(node.right, v);
        }else{
            if(node.left == null){ //If only right leaf
                return node.right;
            }else if(node.right == null){ //If only left leaf
                return node.left;
            }else{ //If two leaves, find right-most value in left leaf sub-tree, and replace.
                node.element = getRightMostInLeftTree(node.left).element;
                node.left = remove(node.left, node.element);
            }
        }
        return node;
    }
    
    private TNode getRightMostInLeftTree(TNode node){
        TNode minimum = node;
        //Traversing until we reach the right most leaf
        while (minimum.right != null){
            minimum = minimum.right;
        }
        return minimum;
    }
    
    /**************************************************/
    /* UNION METHOD */
    
    public BSTSet union(BSTSet s){
        traverseAndAdd(root, s);
        return s;
    }
    //Traverse through the tree and use add method to add each element.
    private void traverseAndAdd(TNode node, BSTSet s){
        if (node == null){ 
            return; 
        }
        traverseAndAdd(node.left, s); 
        s.add(node.element);
        traverseAndAdd(node.right, s);
    }
    
    /**************************************************/
    /* INTERSECTION METHOD */
    
    public BSTSet intersection(BSTSet s){
        //Convert both BST's into array's
        int[] thisArray = inOrderSort(this);
        int[] sArray = inOrderSort(s);
        
        int i = 0, j = 0;
        
        //Finding common elements
        BSTSet r = new BSTSet();
        while(i < thisArray.length && j < sArray.length){
            if(thisArray[i] < sArray[j]){
                i++;
            }else if(thisArray[i] > sArray[j]){
                j++;
            }else{
                r.add(thisArray[i]);
                i++;
                j++;
            }
        }
        return r;
    }
    
    /**************************************************/
    /* DIFFERENCE METHOD */
    
    public BSTSet difference(BSTSet s){
        //Convert both BST's into array's
        int[] thisArray = inOrderSort(this);
        int[] sArray = inOrderSort(s);
        BSTSet r = new BSTSet();
        boolean isDiff;
        
        //Finding elements that differ in the sets
        for(int i = 0; i < thisArray.length; i++){
            isDiff = true;
            for(int j = 0; j < sArray.length; j++){
                if(thisArray[i] == sArray[j]){
                    isDiff = false;
                }
            }
            if(isDiff){
                r.add(thisArray[i]);
            }
        }
        return r;
    }
    
    /**************************************************/
    /* SIZE METHOD */
    
    public int getSize(){
        return getSize(root);
    }
    private int getSize(TNode node){
        if(node == null){
            return 0;
        }
        //The extra 1 is the root
        return getSize(node.left) + 1 + getSize(node.right);
    }
    
    /**************************************************/
    /* HEIGHT METHOD */
    
    public int getHeight(){
        if (root == null) { return -1; }
        return getHeight(root);
    }
    public int getHeight(TNode node){
        int leftHeight;
        int rightHeight;
        if(node == null){
            return 0;
        }else{
            //Find maximum of left and right subtree's and add 1 for the root
            if(getHeight(node.left) > getHeight(node.right)){
                return 1 + getHeight(node.left);
            }else{
                return 1 + getHeight(node.right);
            }
        }
    }
    
    /**************************************************/
    /* PRINTING METHOD */
    public void printBSTSet(){
        if(root==null)
            System.out.println("The set is empty");
        else{
            System.out.print("The set elements are: ");
            printBSTSet(root);
            System.out.print("\n");
        }
    }
    
    private void printBSTSet(TNode t){
        if(t!=null){
            printBSTSet(t.left);
            System.out.print(" " + t.element + ", ");
            printBSTSet(t.right);
        }
    }
    
    /**************************************************/
    /* PRINTING NON-REC METHOD */
    public void printNonRec(){
        Stack s = new Stack();
        TNode node = root;
        
        //Working from left to right, we push each node into the stack, and pop it when traversing back
        while(!(s.isEmpty()) || node != null){
            if(node != null){
                s.push(node);
                node = node.left;
            }else{
                node = s.pop();
                if(node != null){
                    System.out.print(" " + node.element + ", ");
                    node = node.right;
                }    
                
            }
        }
    }
    
    /**************************************************/
    /* HELPER METHODS */
    
    //These are used to convert a BST into an array.
    private int[] inOrderSort(BSTSet s){
        int[] array = new int[s.getSize()];
        traverseAndStore(s.root, array, 0);
        
        return array;
    }
    
    private int traverseAndStore(TNode node, int[] array, int index){
        if(node == null) { return index; }
        index = traverseAndStore(node.left, array, index); 
        array[index++] = node.element;
        index = traverseAndStore(node.right, array, index); 

        return index;
    }
    
    //Tree node class
    public class TNode{
        int element;
        TNode left;
        TNode right;
        TNode(int i, TNode l, TNode r){ 
            element = i;
            left = l; 
            right = r; 
        }
    }
    
    //Stack class
    public class Stack{
        TNode[] elements;
        int indexTop;
        
        public Stack(int n){
            elements = new TNode[n];
        }
        
        public Stack(){
            this(100);
        }
        
        public void push(TNode n){
            if(elements.length - 1 == indexTop){
                TNode[] array = new TNode[2*elements.length];
                for(int i = 0; i < array.length; i++){
                    array[i] = elements[i];
                }
                
                elements = array;
            }
            indexTop++;
            elements[indexTop] = n;
        }
        
        public TNode pop(){
            TNode popped = elements[indexTop];
            elements[indexTop] = null;
            indexTop--;
            return popped;
        }
        
        public boolean isEmpty(){
            return indexTop < 0;
        }
    }
}