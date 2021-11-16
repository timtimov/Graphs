package algorithms;

import java.util.Scanner;


/**
 *
 * @author Eftim
 */

class Queue{
    
    int front = 0;
    int back = 0;
    int count = 0;
    int items;
    int[] queue;

    public Queue(int n) {
        this.items = n;
        this.queue = new int[n];
    }
    
    public boolean isEmpty(){
        return this.count == 0;
    }
    
    public void enqueue(int v){
        this.queue[back] = v;
        this.back = (++this.back) % this.items;
        this.count += 1;
    }
    
    public int dequeue(){
        int v = this.queue[this.front];
        this.queue[this.front] = -1;
        this.front = (++this.front) % this.items;
        this.count -= 1;
        return v;
    }
}

interface Graph{
    
    void pair(int x, int y);
    int countH(int x);
    void info();
    void write(int[][] mat);
    int[][] multi(int[][] mat1, int[][] mat2);
    void walks(int k);
    void init();
    int[] neigh(int v);
    void dfs(int v);
    void dfs_full();
    void bfs(int v);
    void bfs_full();
    void sp(int i);
    void comp();
}

class Undirected implements Graph{
    
    int numVer;
    int Pairs = 0;
    int time = 0;
    
    String dfsV = "";
    String dfsI = "";
    
    String bfs = "";
    int[][] matrix;
    int[] mark;
    
    public Undirected(int n) {
        this.matrix = new int[n][n];
        this.numVer = n;
        this.mark = new int[n];
    }
    
    
    @Override
    public void pair(int x, int y) {
        if(this.matrix[x][y] != 1){
            this.Pairs += 1;
            this.matrix[x][y] = 1;
            this.matrix[y][x] = 1;
        }
    }
    
    @Override
    public int countH(int x) {
        int sum = 0;
        for (int i = 0; i < this.numVer; i++) {
            sum += this.matrix[x][i];
        }
        return sum;
    }
    
    @Override
    public void info() {
        System.out.printf("%d %d %d\n", this.numVer, this.Pairs, ((this.numVer * (this.numVer+1)) / 2) - this.Pairs );
        for (int i = 0; i < this.numVer; i++) {
            System.out.printf("%d %d\n", i, this.countH(i));
        }
    }
    
      @Override
    public void write(int[][] mat) {
        for (int i = 0; i < this.numVer; i++) {
            for (int j = 0; j < this.numVer; j++) {
                System.out.print(mat[i][j] + " ");
            }
            System.out.println("");
        }
    }

    @Override
    public int[][] multi(int[][] mat1, int[][] mat2) {
        int sum = 0;
        int[][] matNew = new int[this.numVer][this.numVer];
        for (int i = 0; i < this.numVer; i++) {
            for (int j = 0; j < this.numVer; j++) {
                for (int k = 0; k < this.numVer; k++) {
                    sum += mat1[i][k] * mat2[k][j];
                }
                matNew[i][j] = sum;
                sum = 0;
            }
        }
        return matNew;
    }

    @Override
    public void walks(int k) {
        int[][] matMul = this.matrix;
        int count = 1;
        while(count != k){
            matMul = multi(this.matrix, matMul);
            count += 1;
        }
        this.write(matMul);
    }

    @Override
    public void init() {
        for (int i = 0; i < this.numVer; i++) {
            this.mark[i] = 0;
        }
    }
    
    @Override
    public int[] neigh(int v) {
        int count = 0;
        int matC = 0;
        for (int i = 0; i < this.numVer; i++) {
            if(this.matrix[v][i] == 1){
                count += 1;
            }
        }
        int[] mat = new int[count];
        for (int j = 0; j < this.numVer; j++) {
            if(this.matrix[v][j] == 1){
                mat[matC++] = j;
            }
        }
        return mat;
    }
    
    @Override
    public void dfs(int v) {
        this.time += 1;
        this.dfsV = this.dfsV + v + " ";
        this.mark[v] = this.time;
        int[] nei = this.neigh(v);
        if(nei.length != 0){
            for (int i : nei) {
                if(this.mark[i] == 0){
                    this.dfs(i);
                }
            }
        }
        this.dfsI = this.dfsI + v + " ";
    }

    @Override
    public void dfs_full() {
        this.init();
        for (int i = 0; i < this.numVer; i++) {
            if(this.mark[i] == 0){
                this.dfs(i);
            }
        }
        System.out.println(this.dfsV);
        System.out.println(this.dfsI);
    }

    @Override
    public void bfs(int v) {
        Queue q = new Queue(this.numVer);
        q.enqueue(v);
        this.time += 1;
        this.mark[v] = this.time;
        while(q.isEmpty() == false){
            int ver = q.dequeue();
            this.bfs = this.bfs + ver + " ";
            if((this.mark[ver] + 1) != this.time){
                this.time += 1;
            }
            int[] nei = this.neigh(ver);
            if(nei.length != 0){
                for (int i : nei){
                    if(this.mark[i] == 0){
                        q.enqueue(i);
                        this.mark[i] = this.time;
                    }
                }
            }
        }
    }

    @Override
    public void bfs_full() {
        this.init();
        for (int i = 0; i < this.numVer; i++) {
            if(this.mark[i] == 0){
                this.bfs(i);
            }
        }
        System.out.println(this.bfs);
    }

    @Override
    public void sp(int i) {
        this.bfs(i);
        for (int j = 0; j < this.numVer; j++) {
            System.out.printf("%d %d\n",  j, this.mark[j] - 1);
        }
    }

    @Override
    public void comp() {
        this.init();
        for (int i = 0; i < this.numVer; i++) {
            if(this.mark[i] == 0){
                this.dfs(i);
                
                for (int j = 0; j < this.numVer; j++) {
                    if(this.mark[j] > 0){
                        System.out.print(j + " ");
                        this.mark[j] = -1;
                    }
                }
                System.out.println("");
            }
        }
    }
    
}

class Directed implements Graph{
    
    int numVer;
    int Pairs = 0;
    int time = 0;
    
    String dfsV = "";
    String dfsI = "";
    
    String bfs = "";
    int[][] matrix;
    int[] mark;

    public Directed(int n) {
        this.matrix = new int[n][n];
        this.numVer = n;
        this.mark = new int[n];
    }
    
    
    @Override
    public void pair(int x, int y) {
        if(this.matrix[x][y] != 1){
            this.matrix[x][y] = 1;
            this.Pairs += 1;
        } 
    }
    
    @Override
    public int countH(int x) {
        int sum = 0;
        for (int i = 0; i < this.numVer; i++) {
            sum += this.matrix[x][i];
        }
        return sum;
    }
    
    public int countV(int x){
        int sum = 0;
        for (int i = 0; i < this.numVer; i++) {
            sum += this.matrix[i][x];
        }
        return sum;
    }
    
    @Override
    public void info() {
        System.out.printf("%d %d %d\n", this.numVer, this.Pairs, (this.numVer*this.numVer) - this.Pairs );
        for (int i = 0; i < this.numVer; i++) {
            System.out.printf("%d %d %d\n", i, this.countH(i), this.countV(i));
        }
    }
    
    @Override
    public void write(int[][] mat) {
        for (int i = 0; i < this.numVer; i++) {
            for (int j = 0; j < this.numVer; j++) {
                System.out.print(mat[i][j] + " ");
            }
            System.out.println("");
        }
    }

    @Override
    public int[][] multi(int[][] mat1, int[][] mat2) {
        int sum = 0;
        int[][] matNew = new int[this.numVer][this.numVer];
        for (int i = 0; i < this.numVer; i++) {
            for (int j = 0; j < this.numVer; j++) {
                for (int k = 0; k < this.numVer; k++) {
                    sum += mat1[i][k] * mat2[k][j];
                }
                matNew[i][j] = sum;
                sum = 0;
            }
        }
        return matNew;
    }
    
    @Override
    public void walks(int k) {
        int[][] matMul = this.matrix;
        int count = 1;
        while(count != k){
            matMul = multi(this.matrix, matMul);
            count += 1;
        }
        this.write(matMul);
    }

    @Override
    public void init() {
        for (int i = 0; i < this.numVer; i++) {
            this.mark[i] = 0;
        }
    }
    
    @Override
    public int[] neigh(int v) {
        int count = 0;
        int matC = 0;
        for (int i = 0; i < this.numVer; i++) {
            if(this.matrix[v][i] == 1){
                count += 1;
            }
        }
        int[] mat = new int[count];
        for (int j = 0; j < this.numVer; j++) {
            if(this.matrix[v][j] == 1){
                mat[matC++] = j;
            }
        }
        return mat;
    }
    
    @Override
    public void dfs(int v) {
        this.dfsV = this.dfsV + v + " ";
        this.mark[v] = this.time;
        int[] nei = this.neigh(v);
        if(nei.length != 0){
            for (int i : nei) {
                if(this.mark[i] == 0){
                    this.dfs(i);
                }
            }
        }
        this.dfsI = this.dfsI + v + " ";
    }

    @Override
    public void dfs_full() {
        this.init();
        for (int i = 0; i < this.numVer; i++) {
            if(this.mark[i] == 0){
                this.time += 1;
                this.dfs(i);
            }
        }
        System.out.println(this.dfsV);
        System.out.println(this.dfsI);
    }

    @Override
    public void bfs(int v) {
        Queue q = new Queue(this.numVer);
        q.enqueue(v);
        this.time += 1;
        this.mark[v] = this.time;
        while(q.isEmpty() == false){
            int ver = q.dequeue();
            this.bfs = this.bfs + ver + " ";
            if((this.mark[ver] + 1) != this.time){
                this.time += 1;
            }
            int[] nei = this.neigh(ver);
            if(nei.length != 0){
                for (int i : nei){
                    if(this.mark[i] == 0){
                        q.enqueue(i);
                        this.mark[i] = this.time;
                    }
                }
            }
        }
    }

    @Override
    public void bfs_full() {
        this.init();
        for (int i = 0; i < this.numVer; i++) {
            if(this.mark[i] == 0){
                this.bfs(i);
            }
        }
        System.out.println(this.bfs);
    }

    @Override
    public void sp(int i) {
        this.bfs(i);
        for (int j = 0; j < this.numVer; j++) {
            System.out.printf("%d %d\n",  j, this.mark[j] - 1);
        }
    }

    @Override
    public void comp() {
        this.init();
        for (int i = 0; i < this.numVer; i++) {
            if(this.mark[i] == 0){
                this.time += 1;
                this.dfs(i);
            }
        }
        String[] revDfsI = this.dfsI.split(" ");
        int[][] tranMat = new int[this.numVer][this.numVer];
        for (int j = 0; j < this.numVer; j++) {
            for (int k = 0; k < this.numVer; k++) {
                tranMat[k][j] = this.matrix[j][k];
            }
        }
        this.matrix = tranMat;
        int[] revD = new int[this.numVer];
        for (int i = 0; i < this.numVer; i++) {
            revD[i] = Integer.parseInt(revDfsI[this.numVer - i - 1]);
        }
        this.init();
        this.time = 0;
        for(int i : revD){
            if(this.mark[i] == 0){
                this.time += 1;
                this.dfs(i);
            }
        }
        for (int i = 0; i < this.numVer; i++) {
            if(this.mark[i] > 0){
                System.out.print(i + " ");
                for (int j = 0; j < numVer; j++) {
                    if((this.mark[j] == this.mark[i]) && (i != j)){
                        System.out.print(j + " ");
                        this.mark[j] = -1;
                    }
                }
                this.mark[i] = -1;
                System.out.println("");
            }
        }
    }
      
}


public class DfsBfs {
    
    public static void main(String[] args) {
        
        Graph graph;
        
        try(Scanner sc = new Scanner(System.in)){
            
            int numV = sc.nextInt();
            sc.nextLine();
            if(args[0].equals("undirected")){
                graph = new Undirected(numV);
            }else{
                graph = new Directed(numV);
            }
            while(sc.hasNextLine()){
                String s = sc.nextLine();
                String[] sn = s.split(" ");
                int x = Integer.parseInt(sn[0]);
                int y = Integer.parseInt(sn[1]);
                graph.pair(x,y);
            }
            
            switch(args[1]){
                case "info": 
                    graph.info();
                    break;
                case "walks":
                    int k = Integer.parseInt(args[2]);
                    graph.walks(k);
                    break;
                case "dfs":
                    graph.dfs_full();
                    break;
                case "bfs":
                    graph.bfs_full();
                    break;
                case "sp":
                    int i = Integer.parseInt(args[2]);
                    graph.sp(i);
                    break;
                case "comp":
                    graph.comp();
                    break;
            }
                    
        }catch(Exception e){
            e.getMessage();
        }
            
    }
    
}
