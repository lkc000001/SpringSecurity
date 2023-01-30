package com.training.session7.lambda.streamApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class NioStreamApi {
	public static void main(String[] args) throws IOException {
		testList();
//		testFind();
//		testWalk();
//		testLines();
//		testNewBufferedReader();
	}
	
	public static void testList() throws IOException {
		// Files.list()列出該路徑下的所有檔案,但只在第一層,並不是以「遞廻」的方式列出各階層
		try (Stream<Path> stream = 
				Files.list(Paths.get("dir/session7"))) {
			stream
				.filter(path -> path.toString().endsWith(".java"))
				.forEach(System.out::println);
		}
	}
	
	public static void testFind() throws IOException {
		// Files.find()找出符合條件的目錄或檔案
		try (Stream<Path> stream = 
			Files.find(
				// 搜尋起始目錄	
				Paths.get("dir/session7"),
				// 所指定的層數進行搜尋，非遞迴搜尋全部
				2,
				// 搜尋條件
				(path, attr) -> path.toString().endsWith(".txt")
			)
		) {
			stream.forEach(System.out::println);
		}
	}
	
	public static void testWalk() throws IOException {
		// 指定要遞廻的層數
		try (Stream<Path> stream = 
				Files.walk(Paths.get("dir/session7/NIO2Demo"), 4)) {
			stream
				.filter(path -> path.toString().endsWith(".txt"))
				.forEach(System.out::println);
		}
		System.out.println("------------------------------------");
		// 不指定層數遞廻全部
		try (Stream<Path> stream = 
				Files.walk(Paths.get("dir/session7/NIO2Demo"))) {
			stream
				.filter(path -> path.toString().endsWith(".txt"))
				.forEach(System.out::println);
		}
	}
	
	public static void testLines() throws IOException {
		// Files.lines()將檔案內容讀出
		try (Stream<String> stream = 
				Files.lines(Paths.get("dir/session7/data.txt"))) {
			stream
				.map(String::toLowerCase)
				.forEach(System.out::println);
		}
	}
	
	

	public static void testNewBufferedReader() throws IOException {
		// BufferedReader也新增了方法lines()回傳Stream<String>
		try (BufferedReader reader = 
				Files.newBufferedReader(Paths.get("dir/session7/data.txt"))) {
			reader
				.lines()
				.map(String::toLowerCase)
				.forEach(System.out::println);
		}
	}
}
