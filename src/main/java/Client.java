import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        //设置超时时间
        socket.setSoTimeout(3000);
        //设置连接的地址（本地）和端口号（2000），以及超时时间（3000）
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 2000), 3000);

        System.out.println("已发起服务器连接，并进入后续流程");
        System.out.println("客户端信息：" + socket.getLocalAddress() + "p:" + socket.getLocalPort());
        System.out.println("服务端信息：" + socket.getInetAddress() + "p:" + socket.getPort());

        //调用todo
        try {
            todo(socket);
        } catch (Exception e) {
            System.out.println("异常关闭");
        }
        //释放资源
        socket.close();
        System.out.println("客户端已退出");

    }

    //发送数据
    private static void todo(Socket Client) throws IOException {
        //构建基础数据输入流
        InputStream in = System.in;
        BufferedReader input = new BufferedReader(new InputStreamReader(in));

        //得到Socket输出流，并转化为打印流
        OutputStream outputStream=Client.getOutputStream();
        PrintStream socketPrintStream=new PrintStream(outputStream);

        //得到服务器端的输入流,并转化为BufferedReader
        InputStream inputStream2=Client.getInputStream();
        BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(inputStream2));

        boolean flag=true;
        do {
            //键盘读取一行
            String str = input.readLine();
            //发送数据到服务器
            socketPrintStream.println(str);

            //从服务器读取一行
            String echo = socketBufferedReader.readLine();
            if ("bye".equalsIgnoreCase(echo)) {
                    flag=false;
            }else {
                System.out.println(echo);
            }
        }
        while (flag);

        //资源释放
        socketPrintStream.close();
        socketBufferedReader.close();
    }
}
