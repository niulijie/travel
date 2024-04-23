import com.sun.glass.ui.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class SelectionSortTest {
    /**
     * 选择排序
     */
    @Test
    public void selectionSortTest(){
        int[] arr = {7,1,3,5,1,6,8,1,3,5,7,5,6};
        printArray(arr);
        selectSort(arr);
        printArray(arr);
    }

    /**
     * 选择排序
     */
    private void selectSort(int[] arr){
        // 1. 先想边界, 如果数组没值或者只有一个值，不用做交换
        if(arr == null || arr.length < 2){
            return;
        }
        //思路：
        // 先找出0 ~ n-1最小数
        // 再找出1 ~ n-1最小数
        // 再找出2 ~ n-1最小数
        int N = arr.length;
        for (int i = 0; i < N; i++) {
            // i可以看作是左侧的数字
            int minValueIndex = i;
            for (int j = i+1; j < N; j++) {
                minValueIndex = arr[j] < arr[minValueIndex] ? j : minValueIndex;
            }
            // 交互2个位置的元素
            if (minValueIndex != i){
                swap(arr, i, minValueIndex);
            }
        }
    }

    @Test
    public void testBabbleSort() {
        int[] arr = {7,1,3,5,1,6,8,1,3,5,7,5,6};
        printArray(arr);
        babbleSort(arr);
        printArray(arr);
    }

    private void babbleSort(int[] arr) {
        // 0 ~ n-1
        // 0 ~ n-2
        // 0 ~ n-3
        // 0 ~ end
        int N = arr.length;
        for (int end = N-1; end >= 0; end--) {
            // 0~end 干一坨事情
            for (int second = 1; second < N; second++) {
                if(arr[second - 1] > arr [second]){
                    swap(arr, second - 1, second);
                }
            }
        }
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
    }

    private void printArray(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
}
