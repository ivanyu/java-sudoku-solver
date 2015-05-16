package me.ivanyu.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Решатель судоку.
 */
public class SudokuSolver {
  // Начальное поле.
  private final Field initialField;

  public SudokuSolver(final Field initialField) {
    assert initialField != null;

    this.initialField = initialField;
  }

  /**
   * Решить судоку.
   * @return одно из возможных решений или null, если решений нет.
   */
  public Field solve() {
    final List<EmptyCell> initialEmptyCells = collectInitialEmptyCells();
    Collections.sort(initialEmptyCells, EmptyCellVariantCountComparator.INSTANCE);
    return solveRec(initialField, initialEmptyCells);
  }

  /**
   * Рекурсивный поиск решения.
   * @param field текущее поле
   * @param emptyCells список пустых клеток, отсортированный по количеству вариантов в них
   * @return одно из возможных решений или null, если решения нет.
   */
  private static Field solveRec(final Field field, final List<EmptyCell> emptyCells) {
    // Не осталось пустых клеток - решение найдено.
    if (emptyCells.size() == 0)
      return field;

    // Делим список пустых клеток на голову и хвост.
    // Голова - первая клетка, которую будем обрабатывать сейчас. Хвост - остальное.
    final EmptyCell ecHead = emptyCells.get(0);
    final List<EmptyCell> ecTail = emptyCells.size() > 1
        ? Collections.unmodifiableList(emptyCells.subList(1, emptyCells.size()))
        : Collections.<EmptyCell>emptyList();

    // Пустая клетка без вариантов - решения нет.
    if (ecHead.variants.size() == 0)
      return null;

    // Перебор варинатов для текущей клетки.
    for (Integer variant : ecHead.variants) {
      final Field updatedField = field.setValue(ecHead.row, ecHead.col, variant);

      // Подготовка списка пустых клеток для следующего шара рекурсии
      final List<EmptyCell> updatedEmptyCells = new ArrayList<>();
      for (EmptyCell ec : ecTail) {
        // Отбрасываем невозможные при данном выборе значения
        // варианты в оставшихся пустых клетках.
        if (Field.cellsInContact(ecHead.row, ecHead.col, ec.row, ec.col)) {
          updatedEmptyCells.add(ec.removeVariantIfExists(variant));
        } else {
          updatedEmptyCells.add(ec);
        }
      }
      Collections.sort(updatedEmptyCells, EmptyCellVariantCountComparator.INSTANCE);

      final Field solution = solveRec(updatedField, updatedEmptyCells);
      // Нашли решение - возвращаем.
      if (solution != null)
        return solution;
    }

    // Ни один вариант не привел к решению - решения нет.
    return null;
  }

  /**
   * Сбор вариантов для пустых клеток начального поля.
   */
  private List<EmptyCell> collectInitialEmptyCells() {
    final List<EmptyCell> result = new ArrayList<>();

    for (int row = 0; row < Field.FIELD_SIZE; row++) {
      for (int col = 0; col < Field.FIELD_SIZE; col++) {
        if (initialField.getValue(row, col) == Field.EMPTY) {
          // Изначально возможно все,
          // но отбрасываем то, что уже есть в строке, столбце, квадрате.
          final List<Integer> variants = new ArrayList<>(Field.POSSIBLE_VALUES);
          variants.removeAll(initialField.getRowValues(row));
          variants.removeAll(initialField.getColValues(col));
          variants.removeAll(initialField.getLocalSquareValues(row, col));
          result.add(new EmptyCell(row, col, variants));
        }
      }
    }

    return result;
  }
}
