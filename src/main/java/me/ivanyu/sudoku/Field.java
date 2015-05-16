package me.ivanyu.sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Поле судоку.
 */
final class Field {
  /** Размер поля */
  public static int FIELD_SIZE = 9;
  /** Размер локального квадрата */
  public static int LOCAL_SQUARE_SIZE = 3;

  /** Значение пустой клетки */
  public static int EMPTY = 0;

  /** Все допустимые значения, включая пустое */
  public static List<Integer> POSSIBLE_VALUES = Collections.unmodifiableList(
      Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, EMPTY));

  private final int[][] values;

  public Field(final int[][] values) {
    assert values.length == FIELD_SIZE;
    for (int[] row : values) {
      assert row.length == FIELD_SIZE;
      for (int v : row) {
        checkValue(v);
      }
    }

    this.values = values;
  }

  /**
   * Получение значения клетки поля
   * @param row строка
   * @param col столбец
   * @return значение
   */
  public int getValue(final int row, final int col) {
    checkDimension(row);
    checkDimension(col);

    return values[row][col];
  }

  /**
   * Установка значения в ячейке поля.
   * @param row строка
   * @param col столбец
   * @param value новое значение
   * @return поле с обновленным значением
   */
  public Field setValue(final int row, final int col, final int value) {
    checkDimension(row);
    checkDimension(col);
    checkValueNotEmpty(value);

    if (values[row][col] != value) {
      // Полностью копируем строки и меняем одну нужную
      final int[][] newValues = Arrays.copyOf(values, FIELD_SIZE);
      newValues[row] = Arrays.copyOf(values[row], FIELD_SIZE);
      newValues[row][col] = value;
      return new Field(newValues);
    } else {
      return this;
    }
  }

  /**
   * Получение всех значений из строки.
   * @param row строка
   * @return список значений в строке {@code row}
   */
  public List<Integer> getRowValues(final int row) {
    checkDimension(row);

    List<Integer> result = new ArrayList<>();
    for (int col = 0; col < FIELD_SIZE; col++) {
      result.add(values[row][col]);
    }
    return Collections.unmodifiableList(result);
  }

  /**
   * Получение всех значений из столбца.
   * @param col столбец
   * @return список значений в столбце {@code col}
   */
  public List<Integer> getColValues(final int col) {
    checkDimension(col);

    List<Integer> result = new ArrayList<>();
    for (int row = 0; row < FIELD_SIZE; row++) {
      result.add(values[row][col]);
    }
    return Collections.unmodifiableList(result);
  }

  /**
   * Получение всех значений из локального квадрата.
   * @param row номер строки (в координатах клеток)
   * @param col номер столбца (в координатах клеток)
   * @return список значений локального квадрата, в который попадает клетка по {@code (row; col)}
   */
  public List<Integer> getLocalSquareValues(final int row, final int col) {
    checkDimension(row);
    checkDimension(col);

    final int sqRow = row / LOCAL_SQUARE_SIZE;
    final int sqCol = col / LOCAL_SQUARE_SIZE;

    List<Integer> result = new ArrayList<>();
    for (int cellRow = sqRow * LOCAL_SQUARE_SIZE; cellRow < (sqRow + 1) * LOCAL_SQUARE_SIZE; cellRow++) {
      for (int cellCol = sqCol * LOCAL_SQUARE_SIZE; cellCol < (sqCol + 1) * LOCAL_SQUARE_SIZE; cellCol++) {
        result.add(values[cellRow][cellCol]);
      }
    }

    return Collections.unmodifiableList(result);
  }

  /**
   * Проверка, влияют ли две клетки друг на друга (по правилам судоку).
   */
  public static boolean cellsInContact(final int row1, final int col1,
                                       final int row2, final int col2) {
    checkDimension(row1);
    checkDimension(col1);
    checkDimension(row2);
    checkDimension(col2);

    final int sqRow1 = row1 / LOCAL_SQUARE_SIZE;
    final int sqCol1 = col1 / LOCAL_SQUARE_SIZE;
    final int sqRow2 = row2 / LOCAL_SQUARE_SIZE;
    final int sqCol2 = col2 / LOCAL_SQUARE_SIZE;

    return row1 == row2 ||
        col1 == col2 ||
        (sqRow1 == sqRow2 && sqCol1 == sqCol2);
  }

  /**
   * Проверка корректности номера строки или столбца.
   */
  private static void checkDimension(final int d) {
    assert d >= 0 && d < FIELD_SIZE;
  }

  /**
   * Проверка корректности значения, считая, что пустые значения разрешены.
   */
  private static void checkValue(final int v) {
    assert POSSIBLE_VALUES.contains(v);
  }

  /**
   * Проверка корректности значения, считая, что пустые значения запрещены.
   */
  private static void checkValueNotEmpty(final int v) {
    assert v != EMPTY;
    assert POSSIBLE_VALUES.contains(v);
  }
}
