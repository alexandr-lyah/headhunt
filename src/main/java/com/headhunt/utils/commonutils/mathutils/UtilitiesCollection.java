package com.headhunt.utils.commonutils.mathutils;

import java.util.*;

/**
 *
 * @author shekhar2010us
 */
public class UtilitiesCollection {

    public static void filterByRank(LinkedHashMap<String, Integer> map, int top) {
        Iterator<Map.Entry<String , Integer>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            top--;
            Map.Entry<String , Integer> entry = iter.next();
            if (top < 0) {
                iter.remove();
            }
        }
    }

    public static void filterByValue(LinkedHashMap<String, Integer> map, int K) {
        Iterator<Map.Entry<String , Integer>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String , Integer> entry = iter.next();
            if(entry.getValue() < K){
                iter.remove();
            }
        }
    }

  /**
   * Sort a Java TreeMap by its value
   * @param map
   * @return Map
   */
  public static Map sortByValue(TreeMap<String, Integer> map) {
    List list = new LinkedList(map.entrySet());
    Collections.sort(list, new Comparator() {
      public int compare(Object o2, Object o1) {
        return ((Comparable) ((Map.Entry) (o1)).getValue())
                .compareTo(((Map.Entry) (o2)).getValue());
      }
    });
    
    Map result = new LinkedHashMap();
    for (Iterator it = list.iterator(); it.hasNext();) {
      Map.Entry entry = (Map.Entry)it.next();
      result.put(entry.getKey(), entry.getValue());
    }
    return result;
  }

    public static Map sortByValueDoubleDouble(TreeMap<Double, Double> map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o2, Object o1) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static Map sortByValueDouble(TreeMap<String, Double> map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o2, Object o1) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static Map sortByValueDoubleAbs(TreeMap<String, Double> map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o2, Object o1) {
                return ( (Comparable) Math.abs((double) ((Map.Entry) (o1)).getValue()) )
                        .compareTo( Math.abs((double) ((Map.Entry) (o2)).getValue()) );
            }
        });

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
  
  public static Map sortByComparator(Map unsortMap) {
    
    List list = new LinkedList(unsortMap.entrySet());
    
    // sort list based on comparator
    Collections.sort(list, new Comparator() {
      public int compare(Object o1, Object o2) {
        return ((Comparable) ((Map.Entry) (o1)).getValue())
                .compareTo(((Map.Entry) (o2)).getValue());
      }
    });
    
    // put sorted list into map again: LinkedHashMap make sure order in which keys were inserted
    
    Map sortedMap = new LinkedHashMap();
    for (Iterator it = list.iterator(); it.hasNext();) {
      Map.Entry entry = (Map.Entry) it.next();
      sortedMap.put(entry.getKey(), entry.getValue());
    }
    return sortedMap;
  }
  
  public static Map sortByValue2(TreeMap<String, String> map) {
    List list = new LinkedList(map.entrySet());
    Collections.sort(list, new Comparator() {
      public int compare(Object o2, Object o1) {
        return ((Comparable) ((Map.Entry) (o1)).getValue())
                .compareTo(((Map.Entry) (o2)).getValue());
      }
    });
    
    Map result = new LinkedHashMap();
    for (Iterator it = list.iterator(); it.hasNext();) {
      Map.Entry entry = (Map.Entry)it.next();
      result.put(entry.getKey(), entry.getValue());
    }
    return result;
  }
  
  /**
   * Inputs ArrayList<ArrayList<String>> and merge lists that are overlapping by more than 50% (or the passed parameter)
   * @param allList
   * @param params
   * @return ArrayList<ArrayList<String>>
   */
  public static ArrayList<ArrayList<String>> mergeListofList(ArrayList<ArrayList<String>> allList, double ... params) {
    
    double minimumOverlapRatio = 0.5;
    if (params.length > 0) {
      minimumOverlapRatio = params[0];
    }
    
    boolean moremerges = true;
    while(moremerges) {
      
      outer:
      for ( int i = 0 ; i < allList.size()-1; ++i ) {
        List<String> listA = allList.get(i);
        for ( int j = (i+1) ; j < allList.size(); ++j ) {
          List<String> listB = allList.get(j);
          if ( isOverlap(listA, listB, minimumOverlapRatio) ) {
            createNewList(i, j, allList);
            moremerges = true;
            break outer;
          }
        }
        moremerges = false;
      }
    }
    return allList;
  }
  
  private static void createNewList(int i, int j, ArrayList<ArrayList<String>> allList) {
    ArrayList<ArrayList<String>> newList = new ArrayList<ArrayList<String>>();
    
    HashSet<String> merged = new HashSet<String>();
    merged.addAll(allList.get(i));
    merged.addAll(allList.get(j));
    newList.add( new ArrayList<String>(merged) );
    for ( int x = 0 ; x < allList.size(); ++x ) {
      if ( x != i && x != j ) {
        newList.add(allList.get(x));
      }
    }
    
    allList.clear();
    allList.addAll(newList);
  }
  
  /**
   * Check if the passed two lists are overlapping by the passed parameter overlapping ratio
   * @param listA
   * @param listB
   * @param minimumOverlapRatio
   * @return boolean
   */
  public static boolean isOverlap( List<String> listA, List<String> listB, double minimumOverlapRatio ) {
    boolean overlap = false;
    double sizeA = (double)listA.size();
    double sizeB = (double)listB.size();
    
    List<String> listTemp = new ArrayList<String>(listA);
    listTemp.retainAll(listB);
    double sizeAB = (double)listTemp.size();
    
    if ( sizeAB/sizeA >= minimumOverlapRatio || sizeAB/sizeB >= minimumOverlapRatio ) {
      overlap = true;
    }
    
    return overlap;
  }
  
  
  /**
   * Returns the overlap of list1 and list2
   * @param list1
   * @param list2
   * @return
   */
  public static double overlapRatio( List<String> list1, List<String> list2 ) {
    
    List<String> tmp = new ArrayList<String>(list1);
    double size1 = (double)tmp.size();
    tmp.retainAll(list2);
    return (tmp.size() / size1);
    
  }

  public static String listToString(List<?> list, String... delim) {
      String delimiter = ",";
      if (delim != null && delim.length == 1) {
          delimiter = delim[0].trim();
      }

      StringBuilder builder = new StringBuilder();
      if (!list.isEmpty()) {
          builder.append(list.get(0));
          if (list.size() > 1) {
              for (int i=1; i<list.size(); ++i) {
                  builder.append(delimiter).append(list.get(i));
              }
          }
      }
      return builder.toString();
  }

    public static String listToString(int[] arr, String... delim) {
        String delimiter = ",";
        if (delim != null && delim.length == 1) {
            delimiter = delim[0].trim();
        }

        StringBuilder builder = new StringBuilder();
        if (arr != null && arr.length > 0) {
            builder.append(arr[0]);
            if (arr.length > 1) {
                for (int i=1; i<arr.length; ++i) {
                    builder.append(delimiter).append(arr[i]);
                }
            }
        }
        return builder.toString();
    }

    public static String listToString(double[] arr, String... delim) {
        String delimiter = ",";
        if (delim != null && delim.length == 1) {
            delimiter = delim[0].trim();
        }

        StringBuilder builder = new StringBuilder();
        if (arr != null && arr.length > 0) {
            builder.append(arr[0]);
            if (arr.length > 1) {
                for (int i=1; i<arr.length; ++i) {
                    builder.append(delimiter).append(arr[i]);
                }
            }
        }
        return builder.toString();
    }

    public static List<Double> getListFromArray(double[] arr) {
        List<Double> list = new ArrayList<>(arr.length);
        for (double d : arr) {
            list.add(d);
        }
        return list;
    }

    public static List<Integer> getListFromArray(int[] arr) {
        List<Integer> list = new ArrayList<>(arr.length);
        for (int d : arr) {
            list.add(d);
        }
        return list;
    }

    public static List<String> getListFromArray(String[] arr) {
        List<String> list = new ArrayList<>(arr.length);
        for (String d : arr) {
            list.add(d);
        }
        return list;
    }

}
