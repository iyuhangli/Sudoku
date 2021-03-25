package sudokugame.example.sudoku.control_sudoku;

import java.util.Random;

public class three_levels{

    private static int[][] all_sudoku=new int[9][9];
    private static int [] ini_array=new int[9];
    private static int[][] half_sudoku=new int[9][9];
    private static int[][] check_or_not=new int[9][9];
    private static int[][] half_row=new int[9][9];
    private static int[][] half_col=new int[9][9];
    private static int[][] half_grid=new int[9][9];

    public  three_levels(int level){
        int[][] it_row=new int[9][9];
        int[][] it_col=new int[9][9];
        int[][] it_grid=new int[9][9];
        for(int temp=0;temp<9;temp++){
            for(int temp1=0;temp1<9;temp1++){
                all_sudoku[temp][temp1]=0;
                check_or_not[temp][temp1]=0;
                it_row[temp][temp1]=0;
                it_col[temp][temp1]=0;
                it_grid[temp][temp1]=0;
            }
        }
        ini_array= new int[]{6,3,5,2,9,1,4,8,7};
        //ini_array= new int[]{1,2,3,4,5,6,7,8,9};
        int random_seeds1=(int)((Math.random()*9));
        int random_seeds2=(int)((Math.random()*9));
        int random_seeds3=(int)((Math.random()*9));
        int random_seeds4=(int)((Math.random()*9));
        int random_seeds5=(int)((Math.random()*9));
        int random_seeds6=(int)((Math.random()*9));
        int random_seeds7=(int)((Math.random()*9));
        int random_seeds8=(int)((Math.random()*9));
        int random_seeds9=(int)((Math.random()*9));
        change_place(0,random_seeds9);
        change_place(1,random_seeds1);
        change_place(2,random_seeds2);
        change_place(3,random_seeds3);
        change_place(4,random_seeds4);
        change_place(5,random_seeds5);
        change_place(6,random_seeds6);
        change_place(7,random_seeds7);
        change_place(8,random_seeds8);

        while(!do_generate(it_row,it_col,it_grid)){};
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                half_sudoku[i][j]=all_sudoku[i][j];
                half_row[i][j]=it_row[i][j];
                half_col[i][j]=it_col[i][j];
                half_grid[i][j]=it_grid[i][j];

                System.out.print(all_sudoku[i][j]);
            }
            System.out.println("");
        }
        System.out.println();

        if(level==1) {
            do_get_puzzle();
        }
        else if(level==2) {
            do_get_puzzle_medium();
        }
        else if(level==3){
            do_get_puzzle_hard();
        }
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                System.out.print(half_sudoku[i][j]);
            }
            System.out.println("");

        }
    }

    public static int[][] get_puzzle(){
        return new int[][]{{0,6,3,2,0,5,4,0,0},{0,0,4,6,3,0,5,1,2},{1,0,2,8,4,7,9,6,0},{0,9,1,8,0,0,0,0,4},{0,6,0,0,0,0,0,2,1},{0,8,0,0,3,1,5,7,9},{0,0,0,5,0,9,0,2,0},{2,0,8,1,0,3,4,0,6},{0,1,5,4,2,0,0,0,0}};
    }


    public static int[][] get_all_puzzle(){
        return all_sudoku;
    }
    public static int[][] get_half_puzzle(){
        return half_sudoku;
    }


    private static void change_place(int p1, int p2){
        int temp_change;
        temp_change=ini_array[p1];
        ini_array[p1]=ini_array[p2];
        ini_array[p2]=temp_change;
    }

    private static boolean do_generate(int[][] it_row, int[][] it_col, int[][] it_grid) {
        int row;
        int col;
        int grid;
        int value;
        Random seeds=new Random();
        for(int times_of_ini=0;times_of_ini<11;){
            row=(int)((Math.random()*9));
            col=(int)((Math.random()*9));
            value=seeds.nextInt(9)+1;
            grid=((row/3)*3)+(col/3);
            if(all_sudoku[row][col]==0&&it_row[row][value-1]==0&&it_col[col][value-1]==0&&it_grid[grid][value-1]==0){
                all_sudoku[row][col]=value;
                it_row[row][value-1]=1;
                it_col[col][value-1]=1;
                it_grid[grid][value-1]=1;
                times_of_ini++;
            }
        }
        return iteration(all_sudoku, it_row, it_col, it_grid) == 0;
    }

    private static int iteration(int [][] all_sudoku, int[][] it_row, int[][] it_col, int[][] it_grid) {
        int input_number;
        int temp_number=0;
        int each_grid;
        for (int each_row=0;each_row<9;each_row++){
            for(int each_col=0;each_col<9;each_col++){
                each_grid=((each_row/3)*3)+(each_col/3);
                if(all_sudoku[each_row][each_col]==0){
                    while (++temp_number<10){
                        input_number=ini_array[temp_number-1];
                        if(it_row[each_row][input_number-1]==0&&it_col[each_col][input_number-1]==0&&it_grid[each_grid][input_number-1]==0){
                            it_row[each_row][input_number-1]=1;
                            it_col[each_col][input_number-1]=1;
                            it_grid[each_grid][input_number-1]=1;
                            all_sudoku[each_row][each_col]=input_number;
                            if(iteration(all_sudoku, it_row,it_col,it_grid)==0){
                                return 0;
                            }
                            else{
                                it_row[each_row][input_number-1]=0;
                                it_col[each_col][input_number-1]=0;
                                it_grid[each_grid][input_number-1]=0;
                                all_sudoku[each_row][each_col]=0;
                            }
                        }
                    }
                    return -1;
                }
            }
        }
        return 0;
    }

    private static void do_get_puzzle(){
        int random_x,random_y;

        int easy_level=0;
        while (easy_level<30){
            random_x = (int) (Math.random() * 9);
            random_y = (int) (Math.random() * 9);
            if(check_or_not[random_x][random_y]==0&&half_sudoku[random_x][random_y] != 0) {
                check_or_not[random_x][random_y]=1;
                half_sudoku[random_x][random_y] = 0;
                if (!check_only_one_solution(random_x,random_y)) {
                    half_sudoku[random_x][random_y] = all_sudoku[random_x][random_y];
                }
                else {
                    easy_level++;
                }
            }

        }
    }

    private static void do_get_puzzle_medium(){//81-34=47 even line 8th can't dig
        for(int i = 0 ;i < 9 ; i ++){
            if( i % 2 == 0){
                for(int j = 0 ;j < 6 ; j ++){
                    int tempY = (int) (Math.random() * 100 % 9);
                    if(tempY == 8 || half_sudoku[i][tempY]==0)
                    {
                        j--;
                        continue;
                    }
                    half_sudoku[i][tempY] = 0;
                    if (!check_only_one_solution(i, tempY)) {
                        j--;
                        half_sudoku[i][tempY]=all_sudoku[i][tempY];
                    }
                }
            }
            else{
                for(int j = 0 ;j < 4 ; j ++){
                    int tempY = (int) (Math.random() * 100 % 9);
                    if(tempY == 0|| half_sudoku[i][tempY]==0){
                        j--;
                        continue;
                    }
                    half_sudoku[i][tempY]=0;
                    if (!check_only_one_solution(i, tempY)) {
                        j--;
                        half_sudoku[i][tempY]=all_sudoku[i][tempY];
                    }
                }
            }
        }
        for(int i = 0 ;i < 1 ; i ++){
            int tempX = (int) (Math.random() * 100 % 9);
            int tempY = (int) (Math.random() * 100 % 9);
            if(half_sudoku[tempX][tempY]==0){
                i--;
                continue;
            }
            else{
                half_sudoku[tempX][tempY]=0;
                if (!check_only_one_solution(i, tempY)) {
                    i--;
                    half_sudoku[tempX][tempY]=all_sudoku[tempX][tempY];
                }
            }
        }
    }

    private static void do_get_puzzle_hard(){//81-26=55 each row dig 6
        for(int i = 0 ;i < 9; i++){
            for(int j = 0 ; j < 6 ;j ++){
                int tempY = (int)(Math.random()*9);
                if(half_sudoku[i][tempY]==0){
                    j--;
                    continue;
                }
                half_sudoku[i][tempY]=0;
                if (!check_only_one_solution(i, tempY)) {
                    j--;
                    half_sudoku[i][tempY]=all_sudoku[i][tempY];
                }

            }

        }
        for(int j  = 0 ;j <1 ; j++){//1 is randomly null;
            int tempY = (int)(Math.random()*9);
            int tempX = (int)(Math.random()*9);
            if(half_sudoku[tempX][tempY]==0){
                j--;
                continue;
            }
            half_sudoku[tempX][tempY]=0;
            if (!check_only_one_solution(tempX, tempY)) {
                j--;
                half_sudoku[tempX][tempY]=all_sudoku[tempX][tempY];
            }
        }
    }


    private static boolean check_only_one_solution(int random_x,int random_y) {
        half_row[random_x][all_sudoku[random_x][random_y]-1]=0;
        half_col[random_y][all_sudoku[random_x][random_y]-1]=0;
        half_grid[(random_x/3)*3+(random_y/3)][all_sudoku[random_x][random_y]-1]=0;
        for(int temp_check_solution=0;temp_check_solution<9;temp_check_solution++){
            if((temp_check_solution!=all_sudoku[random_x][random_y]-1)&&half_row[random_x][temp_check_solution]==0&&half_col[random_y][temp_check_solution]==0&&half_grid[(random_x/3)*3+(random_y/3)][temp_check_solution]==0){
                half_row[random_x][temp_check_solution]=1;
                half_col[random_y][temp_check_solution]=1;
                half_grid[(random_x/3)*3+(random_y/3)][temp_check_solution]=1;
                if(iteration(half_sudoku,half_row,half_col,half_grid)==1){
                    return false;
                }
                half_row[random_x][temp_check_solution]=0;
                half_col[random_y][temp_check_solution]=0;
                half_grid[(random_x/3)*3+(random_y/3)][temp_check_solution]=0;
            }
        }
        return true;
    }

}