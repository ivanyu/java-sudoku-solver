package me.ivanyu.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Пустая клетка с вариантами значений.
 */
final class EmptyCell {
  public final int row;
  public final int col;
  public final List<Integer> variants;

  public EmptyCell(final int row, final int col, final List<Integer> variants) {
    for (Integer v : variants) {
      assert Field.POSSIBLE_VALUES.contains(v);
    }

    this.row = row;
    this.col = col;
    this.variants = Collections.unmodifiableList(variants);
  }

  /**
   * Удаление варианта, если он существует.
   * @param variant удаляемый вариант
   * @return объект клетки без удаляемого варианта
   */
  public EmptyCell removeVariantIfExists(final Integer variant) {
    if (variants.contains(variant)) {
      List<Integer> updatedVariants = new ArrayList<>(variants);
      updatedVariants.remove(variant);
      return new EmptyCell(row, col, Collections.unmodifiableList(updatedVariants));
    } else {
      return this;
    }
  }
}
