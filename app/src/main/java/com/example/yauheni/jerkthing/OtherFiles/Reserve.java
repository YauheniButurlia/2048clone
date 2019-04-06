package com.example.yauheni.jerkthing.OtherFiles;

import com.example.yauheni.jerkthing.Cell;

import java.util.Collections;
import java.util.List;

public class Reserve {


  public static void swipeLeft(List<Cell> list){

    clear(list);

    for(Cell cell : list){
      if(cell.getValue().equals(0)){
        continue;
      }
      if(list.indexOf(cell) == 0){
        continue;
      }

      if(!isThereAnyCellLeftTo(list,list.indexOf(cell))){ //isThereACellLeftTo()
        list.get(0).setValue(cell.getValue());
        cell.setNull();
      } else {
        Cell closestCell = getClosestCell(list, cell);
        int closestCellIndex = list.indexOf(closestCell);

        if(closestCellValueEqualToCurrentCellValue(list, cell)){
          if(closestCell.haventBeenCollapsed()){
            closestCell.doubleValue();
            cell.setNull();
          } else {
            list.get(closestCellIndex+1).setValue(cell.getValue());
            cell.setNull();
          }
        } else {
          if(cellsAreNearEachOther(list, closestCell, cell)){
            continue;
          } else {
            list.get(closestCellIndex+1).setValue(cell.getValue());
            cell.setNull();
          }
        }
      }

    }
  }

  private static void printRow(List<Cell> list){
    for(Cell c : list){
      System.out.print("|");
      System.out.print(c.getValue());
    }
  }

  private static boolean isThereAnyCellLeftTo(List<Cell> list, int currentIndex){
    for(int i = currentIndex-1; i >= 0; i--){
      if(list.get(i).getValue().equals(0)){
        continue;
      } else {
        return true;
      }
    }
    return false;
  }

  private static boolean closestCellValueEqualToCurrentCellValue(List<Cell> list, Cell cell){
    return getClosestCell(list, cell).getValue().equals(cell.getValue());
  }

  private static Cell getClosestCell(List<Cell> list, Cell cell){
    for(int i = list.indexOf(cell)-1; i >= 0; i--){
      if(list.get(i).getValue().equals(0)){
        continue;
      } else {
        return list.get(i);
      }
    }
    return null;
  }

  private static boolean cellsAreNearEachOther(List<Cell> list, Cell closestCell, Cell currentCell){
    return list.get(list.indexOf(closestCell)+1).equals(currentCell);
  }

  public static void swipeRight(List<Cell> list){
    Collections.reverse(list);
    swipeLeft(list);
    Collections.reverse(list);
  }

  private static void clear(List<Cell> list){
    for(Cell cell : list) {
      cell.clearCollapsed();
    }
  }
}
