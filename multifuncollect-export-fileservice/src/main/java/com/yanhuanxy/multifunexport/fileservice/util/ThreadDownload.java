package com.yanhuanxy.multifunexport.fileservice.util;

import com.squareup.okhttp.*;
import com.yanhuanxy.multifunexport.tools.util.ThreadPoolUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadDownload {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPool = ThreadPoolUtils.getThreadPool();
        List<Callable<Integer>> tasks = new ArrayList<>();
        int chunkTotal = 100;
        for (int i = 1; i < 50; i++) {
            int maxIndex = i * chunkTotal / 3;
//            initIndex = (i-1) * (chunkTotal / 3) + 1;
            String fileName = i + ".tmp";
            tasks.add(()->{

//                int index = initIndex;
//                while (index <= maxIndex){

                    String url = "http://192.168.0.8:7878/ireport/irtheme/list";
                    try {
                        requestHttp(url, fileName);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
//                    index++;
//                }
                return maxIndex;
            });
        }

        try {
            // 等待所有线程执行完毕
            List<Future<Integer>> futures = threadPool.invokeAll(tasks, 30, TimeUnit.MINUTES);
            for (Future<Integer> item : futures) {
                try {
                    Integer integer = item.get();
                    System.out.println(integer);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    System.out.println("线程内部异常");
                }
            }
        } catch (InterruptedException e) {
            System.out.println("线程等待异常！");
            e.printStackTrace();
        }
//        String url = "https://asset.kelven1.cn:6111/api/check/asset/statement/download/chunk/45f0aa8642eca192b2e246051de01d39?chunkTotal=14&chunkIndex=1";
//
//        executeHttp(url, "1.tmp");
    }

    private static void requestHttp(String url, String fileName) throws IOException {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(10,TimeUnit.SECONDS);
        client.setReadTimeout(5,TimeUnit.SECONDS);
        client.setWriteTimeout(5,TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJkZXB0TmFtZSI6bnVsbCwiY29kZSI6ImNtX2hkbSIsInNjb3BlIjpbImFwcCJdLCJuYW1lIjoi6Z-p5Yas5qKFIiwiZGVwdElkIjo5LCJpZCI6MjE5LCJidXJlYXUiOm51bGwsImV4cCI6MTczMTk4ODUwNSwiYXV0aG9yaXRpZXMiOlsiVVNFUiJdLCJqdGkiOiIzYTZjMTlhZi0xZjA2LTQzMDctYjg0Ni0zNTM5NmRiZDRlMWIiLCJjbGllbnRfaWQiOiJjbG91ZFdlYiJ9.WvLYJSUVHrt8ZdcdWEtWPJ6PN0VxTKIz77-WCyf_z6mAnslBriPHK5IzLANzQumhPw8Miug1kWEky1SHol9TeS3pjk5C3BygCfFWBvSSyk-t-S-vJ4Xczcg7xVnapsj1XYRJzonnh9byS9SeiG8xXuHaUU-qrI3gw3ZgAyLiXo9KyTwmezQ01T-47eLY4w6t_3XjJz-NR40V6sQJiLfk0yfZdU9fTq4NkyyHuid6922U1zlLs4naqIk2K01apQSg7azRLMgEjHcqIQ55R0madmHVVM5vUkbwOyyXv4EmUJOyu-PgOUpPcnPF-rSVLBt8wunKe1k6v5TrqkdTuVouxw")
                .build();
        Response execute = client.newCall(request).execute();
        System.out.println(execute.body());
    }

    private static void executeHttp(String url, String fileName){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Authorization","eyJhbGciOiJIUzUxMiJ9.eyJMT0dJTl9VU0VSX0tFWV8iOiIyNGQwZmQ4ZS1iYzg1LTQ3MmMtOTQwMS01NGM0MmE3OGVmZDQiLCJjbGFpbXNfY291bnRfa2V5IjoiZW5naW5lZXIiLCJpc3MiOiJhc3NldCIsInN1YiI6IlVTRVJOQU1FIiwiYXVkIjoiV2ViIiwiaWF0IjoxNzA2MDc1NzYwfQ.NrcRfd24q7RxkKyKaA5WDiQ-PDnzp9FmVsP1G3NZRqe9nN1idB6T1rUcIb89AdT-NX58o7y8Y9AhF3J-jZ1tqA")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    InputStream inputStream = responseBody.byteStream();
                    FileOutputStream outputStream = new FileOutputStream("D:\\asset\\test1".concat(File.separator).concat(fileName));

                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                }
            } else {
                // 请求失败时的处理
                System.out.println("下载请求失败: " + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            // 异常处理
            e.printStackTrace();
        }
    }
}
