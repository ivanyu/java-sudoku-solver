package me.ivanyu.sudoku;

import java.util.Comparator;

/**
 * Компаратор для пустых клеток, сравнивающий количество возможных вариантов.
 */
final class EmptyCellVariantCountComparator implements Comparator<EmptyCell> {
  public static final EmptyCellVariantCountComparator INSTANCE =
      new EmptyCellVariantCountComparator();

  private EmptyCellVariantCountComparator() {
  }

  @Override
  public int compare(final EmptyCell o1, final EmptyCell o2) {
    return o1.variants.size() - o2.variants.size();
  }
}
