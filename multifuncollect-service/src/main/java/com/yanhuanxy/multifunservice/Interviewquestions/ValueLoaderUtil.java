package com.yanhuanxy.multifunservice.Interviewquestions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ValueLoaderUtil {

    interface ValueLoader<T> {

        List<T> load(long offset, int limit);
    }

    static class LazyValueLoader<T> implements Iterable<T>{

        private final ValueLoader<T> valueLoader;

        /**
         * 数量
         */
        private final int limit;

        /**
         * 偏移量
         */
        private long offset;


        public LazyValueLoader(ValueLoader<T> valueLoader, int limit) {
            this.valueLoader = valueLoader;
            this.limit = limit;
            this.offset = 0;
        }


        @Override
        public Iterator<T> iterator() {
            return new InnerIterable();
        }

        private class InnerIterable implements Iterator<T>{

            private List<T> currentBatch;
            private int currentIndex;

            public InnerIterable() {
                currentBatch = valueLoader.load(offset, limit);
                currentIndex = 0;
            }

            public boolean hasNext() {
                if (currentBatch == null) {
                    return false; // 遍历完成
                }
                if (currentIndex < currentBatch.size()) {
                    return true; // 当前批次还有数据
                }
                // 获取下一批数据
                offset += currentBatch.size();
                currentBatch = valueLoader.load(offset, limit);
                currentIndex = 0;
                return currentBatch != null && !currentBatch.isEmpty();
            }

            public T next() {
                if (hasNext()) {
                    return currentBatch.get(currentIndex++);
                }
                return null;
            }
        }
    }

    class LazyValueLoader2<T> implements Iterable<T>{

        private final ValueLoader<T> valueLoader;

        private long currentOffset;

        private final int limit;

        private boolean hasNext;

        private List<T> currentBatch;

        public LazyValueLoader2(ValueLoader<T> valueLoader, int limit) {
            this.valueLoader = valueLoader;
            this.currentOffset = 0;
            // 设置每次加载的数据数量，可以根据需要调整
            this.limit = limit;
            this.hasNext = true;
            this.currentBatch = null;
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                private int currentIndex = 0;

                @Override
                public boolean hasNext() {
                    loadNextBatchIfNeeded();
                    return hasNext;
                }

                @Override
                public T next() {
                    loadNextBatchIfNeeded();
                    if (!hasNext) {
                        throw new java.util.NoSuchElementException();
                    }
                    T value = currentBatch.get(currentIndex);
                    currentIndex++;
                    if (currentIndex >= currentBatch.size()) {
                        currentOffset += currentBatch.size();
                        currentBatch = null;
                    }
                    return value;
                }
            };
        }

        private void loadNextBatchIfNeeded() {
            if (currentBatch == null || currentBatch.isEmpty()) {
                currentBatch = valueLoader.load(currentOffset, limit);
                if (currentBatch == null || currentBatch.isEmpty()) {
                    hasNext = false;
                } else {
                    hasNext = true;
                }
            }
        }
    }

    class DemoData{
        private Integer num;

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }
    }

    public static void main(String[] args) {
        ValueLoader<DemoData> valueLoader = new ValueLoader() {
            @Override
            public List load(long offset, int limit) {
                // 分页加载数据
                List<DemoData> total = new ArrayList<>();

                return total;
            }
        };

        ValueLoaderUtil.LazyValueLoader<DemoData> lazyIterable = new ValueLoaderUtil.LazyValueLoader<DemoData>(valueLoader, 10);

        for (DemoData demoData : lazyIterable){
            System.out.println(demoData.getNum());
        }

    }

}
