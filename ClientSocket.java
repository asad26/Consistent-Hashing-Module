import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientSocket {

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		//get the localhost IP address, if server is running on some other IP, you need to use that
		InetAddress host = InetAddress.getLocalHost();
		Socket socket = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		
		Scanner scanner = new Scanner(new File("input.xml"), "UTF-8");
		String readMessage = scanner.useDelimiter("\\A").next();
		scanner.close();

		socket = new Socket(host.getHostName(), 9876);
		//write to socket using ObjectOutputStream
		oos = new ObjectOutputStream(socket.getOutputStream());
		System.out.println("Sending request to Socket Server");
		oos.writeObject(readMessage);
		System.out.println("Message sent: \n" + readMessage);

		//read the server response message
		ois = new ObjectInputStream(socket.getInputStream());
		String message = (String) ois.readObject();
		System.out.println("Message: \n" + message);
	
		ois.close();
		oos.close();
		socket.close();
	}

}
