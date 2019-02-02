import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

public class WebServer{
	static final int port = 8080;

	public static void main(String[] args) {
		try {
			ServerSocket serveSock = new ServerSocket(port);
			Socket sock = serveSock.accept();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String input = br.readLine();
			String[] tokens = input.split(" ");
			System.out.println(input);

			String method = tokens[0];
			if (method.equals("GET")){
				String fileName = tokens[1];
				StringBuilder sb = new StringBuilder("files");
				sb.append(tokens[1]);
				fileName = sb.toString();
				File file = new File(fileName);
				byte[] stuff = Files.readAllBytes(file.toPath());
				PrintWriter out = new PrintWriter(sock.getOutputStream());

				out.print(getHeader(stuff.length));
				// need a blank line before content
				out.println(); 
				out.flush();

				BufferedOutputStream dataStream = new BufferedOutputStream(sock.getOutputStream());

				dataStream.write(stuff,0,stuff.length);
				dataStream.flush();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	} 

	/* theres probably a better way to do this */
	public static String getHeader(int length) {
		StringBuilder sb = new StringBuilder("");
		appendLine(sb, "HTTP/1.1 200 OK");
		appendLine(sb, "Server: Java HTTP Server from SSaurel : 1.0");
		appendLine(sb, "Date: " + new Date() );
		appendLine(sb, "Content-type: text/html");
		appendLine(sb, "Content-length: " + length);
		return sb.toString();
	}
	private static void appendLine(StringBuilder sb, String line){
		sb.append(line + "\n");
	}
}