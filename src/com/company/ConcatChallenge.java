package com.company;

import com.sun.istack.internal.NotNull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ConcatChallenge {

    private static final String FILE_PATH = "C:\\Users\\sergieny\\Downloads\\words.txt";
    private static ArrayList<String> alfBethList = new ArrayList<>();
    private static HashMap<IndexOfEntrance, Boolean> temporaryRepository = new HashMap<>();
    static class IndexOfEntrance {
        int start;
        int end;

        IndexOfEntrance(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IndexOfEntrance that = (IndexOfEntrance) o;
            return start == that.start &&
                    end == that.end;
        }

    }

    public static void main(String[] args) {
        customSearch();
    }

    //рекурсивный подбор возможных комбинаций и сравнение со списком слов (кастомная пародия на алгоритм Ахо-Корасик-а)
    private static boolean generateCombinations(@NotNull String word, int first, int finals, HashMap<IndexOfEntrance, Boolean> repository) {

        IndexOfEntrance item = new IndexOfEntrance(first, finals);

        if(word.length() == 0) {
            return true;
        }

        if (word.length() < 2)
            return false;


        if (repository.containsKey(item)) {
            return repository.get(item);
        }
        if (binary(word, alfBethList, 0,alfBethList.size()-1) != -1) {
            repository.put(item, true);
            return true;
        }
        int start = 0;
        int end = word.length();
        for (int i = start; i < end - 1; i++) {
            if (binary(word.substring(start, i+1), alfBethList, 0,alfBethList.size()-1) != -1
                    && generateCombinations(word.substring(i++), i + (first +1), i + finals, repository)) {
                repository.put(item, true);
                return true;
            }
        }

        repository.put(item, false);
        return false;
    }

    private static int binary(String str, ArrayList<String> alfaBeth, int start, int end) {
        if(start > end)
            return -1;
        int mid = start + (end - start)/2;

        int result = alfaBeth.get(mid).compareTo(str);
        if(result == 0)
            return mid;
        else if(result < 0)
            return binary(str, alfaBeth, mid+1, end);
        else
            return binary(str, alfaBeth, start, mid-1);
    }

    //собственно сама проверка и вычитка из файла
    private static void customSearch() {

        try {
            Files.lines(Paths.get(FILE_PATH), StandardCharsets.UTF_8).forEach(alfBethList::add);
            alfBethList.sort(String::compareTo);
            ArrayList<String> sortedList = new ArrayList<>(alfBethList);
            Comparator<String> customComp = (o1, o2) -> Integer.compare(o2.length(), o1.length());
            sortedList.sort(customComp);
            int register = 0;
            while (sortedList.size() > 0) {
                String word = sortedList.remove(0);
                int index = binary(word, alfBethList, 0, alfBethList.size()-1);
                alfBethList.remove(index);

                if (generateCombinations(word, 0, word.length() - 1, temporaryRepository)) {
                    register += 1;
                    if (register == 1)
                        System.out.println(register + ": " + word);
                    else if (register == 2)
                        System.out.println(register + ": " + word);
                }
            }
            System.out.println("Count: " + register);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
