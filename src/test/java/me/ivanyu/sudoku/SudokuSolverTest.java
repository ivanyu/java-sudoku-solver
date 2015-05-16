package me.ivanyu.sudoku;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SudokuSolverTest {
  private final List<Field> testCases = Arrays.asList(
      new Field(new int[][] {
          { 7, 0, 0, 3, 0, 0, 2, 0, 6 },
          { 0, 0, 2, 0, 5, 8, 0, 0, 0 },
          { 8, 3, 0, 0, 0, 7, 0, 4, 9 },
          { 3, 9, 0, 0, 0, 0, 8, 5, 4 },
          { 0, 0, 0, 7, 0, 3, 0, 0, 0 },
          { 1, 2, 8, 0, 0, 0, 0, 6, 7 },
          { 6, 8, 0, 5, 0, 0, 0, 2, 3 },
          { 0, 0, 0, 8, 9, 0, 4, 0, 0 },
          { 4, 0, 5, 0, 0, 1, 0, 0, 8 },
      }),

      new Field(new int[][] {
          { 6, 3, 4, 0, 1, 5, 0, 0, 0 },
          { 0, 0, 0, 6, 4, 0, 5, 0, 9 },
          { 5, 0, 1, 2, 7, 8, 0, 0, 3 },
          { 4, 0, 7, 3, 0, 9, 0, 8, 1 },
          { 9, 8, 0, 4, 2, 1, 0, 5, 7 },
          { 3, 0, 2, 8, 0, 7, 4, 9, 6 },
          { 0, 2, 5, 0, 8, 0, 9, 0, 0 },
          { 8, 6, 3, 0, 9, 0, 1, 7, 2 },
          { 0, 4, 0, 0, 3, 2, 0, 6, 0 },
      }),

      new Field(new int[][] {
          { 0, 3, 4, 0, 1, 0, 0, 0, 0 },
          { 0, 0, 0, 6, 0, 0, 5, 0, 9 },
          { 5, 0, 1, 0, 7, 0, 0, 0, 3 },
          { 4, 0, 7, 0, 0, 0, 0, 8, 1 },
          { 9, 0, 0, 0, 2, 0, 0, 5, 7 },
          { 3, 0, 2, 8, 0, 7, 0, 9, 6 },
          { 0, 2, 5, 0, 8, 0, 0, 0, 0 },
          { 8, 6, 3, 0, 9, 0, 1, 0, 2 },
          { 0, 4, 0, 0, 3, 0, 0, 6, 0 },
      }),

      new Field(new int[][] {
          { 0, 6, 7, 8, 0, 0, 5, 4, 0 },
          { 2, 0, 0, 0, 3, 0, 0, 0, 7 },
          { 0, 4, 9, 0, 7, 0, 8, 0, 0 },
          { 0, 3, 0, 0, 0, 7, 9, 8, 4 },
          { 0, 0, 0, 2, 0, 5, 0, 0, 0 },
          { 7, 8, 6, 4, 0, 0, 0, 1, 0 },
          { 0, 0, 1, 0, 5, 0, 4, 2, 0 },
          { 8, 0, 0, 0, 4, 0, 0, 0, 3 },
          { 0, 9, 3, 0, 0, 2, 1, 5, 0 },
      }),

      new Field(new int[][] {
          { 0, 4, 8, 3, 0, 6, 0, 5, 0 },
          { 0, 0, 9, 0, 2, 0, 6, 0, 8 },
          { 0, 0, 2, 0, 1, 0, 0, 0, 7 },
          { 2, 0, 6, 0, 3, 0, 0, 0, 5 },
          { 0, 0, 3, 0, 0, 9, 8, 0, 0 },
          { 8, 0, 0, 0, 7, 4, 9, 0, 2 },
          { 5, 0, 0, 0, 8, 0, 7, 0, 0 },
          { 9, 0, 4, 0, 6, 0, 5, 0, 0 },
          { 0, 8, 0, 5, 0, 2, 1, 6, 0 },
      }),

      new Field(new int[][] {
          { 4, 0, 0, 2, 0, 0, 0, 3, 0 },
          { 0, 0, 0, 0, 0, 3, 0, 0, 4 },
          { 0, 6, 0, 7, 0, 0, 0, 0, 9 },
          { 0, 0, 1, 8, 5, 0, 6, 0, 0 },
          { 0, 0, 5, 4, 0, 0, 2, 0, 0 },
          { 0, 0, 7, 0, 1, 0, 3, 0, 0 },
          { 1, 0, 0, 0, 0, 9, 0, 5, 0 },
          { 3, 0, 0, 1, 0, 0, 0, 0, 0 },
          { 0, 7, 0, 0, 0, 4, 0, 0, 3 },
      })
  );


  @Test
  public void test() {
    for (Field testCase : testCases) {
      final SudokuSolver solver = new SudokuSolver(testCase);
      final Field solution = solver.solve();
      checkSolution(solution);
    }
  }

  private void checkSolution(Field field) {
    final int correctSum = sum(Field.POSSIBLE_VALUES);
    for (int d = 0; d < Field.FIELD_SIZE; d++) {
      Assert.assertEquals(sum(field.getColValues(d)), correctSum);
      Assert.assertEquals(sum(field.getRowValues(d)), correctSum);
    }

    for (int row = 0; row < Field.FIELD_SIZE; row++) {
      for (int col = 0; col < Field.FIELD_SIZE; col++) {
        Assert.assertEquals(sum(field.getLocalSquareValues(row, col)), correctSum);
      }
    }
  }

  private static int sum(final List<Integer> list) {
    int result = 0;
    for (Integer v : list)
      result += v;
    return result;
  }
}
