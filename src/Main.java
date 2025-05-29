import com.zxg.annotations.Vehicle;
import sun.reflect.Reflection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

class Car implements Vehicle {
    String name;
    public Car(String name) {
        this.name = name;
    }
    @Override
    public void createVehicle() {
        System.out.println("Creating " + name);
    }
}

class Bike implements Vehicle {

    String name;
    public Bike(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void createVehicle() {
        System.out.println("Creating " + name);
    }

    public boolean equals(Bike b) {
        return b.name.length() > this.name.length();
    }
}

class VehicleFactory {
    public static Vehicle getVehicle(String type) {
        Vehicle vehicle;
        if (type.equals("Bike")) {
            vehicle = new Bike("Ninja");
        } else {
            vehicle = new Car("Honda");
        }
        return vehicle;
    }
}

class Logger implements Serializable {
    private static Long serialVersionUID = -12121212128323L;
    private String name;
    private static Integer counter = 0;
    private static Logger logger;

    private Logger (String name) {
        this.name = name;
        counter++;
        System.out.println("No of counters: "+ counter);
    }

    public void runLog() {
        System.out.println("Logger Name: "+ name);
    }

    private static class InstanceHelper {
        private static Logger logger = new Logger("Logger");
        public static Logger getLoggerInstance() {
            return logger;
        }
    }

    public static Logger getLogger (String name) {
        if (Logger.logger == null) {
            synchronized (Logger.class) {
                if (Logger.logger == null) {
                    Logger.logger = new Logger(name);
                }
            }
        }
        return logger;
//        return InstanceHelper.getLoggerInstance();
    }

    public void log(String message) {
        System.out.println(this.name + ": " + message);
    }

    public Object readResolve() {
        return getLogger("");
    }
}

/*
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Logger logger = Logger.getLogger("Logger1");
                logger.log("Starting Program from user 1");
                logger.runLog();
            }
        };

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                Logger logge2 = Logger.getLogger("Logger2");
                logge2.log("Starting Program from user 1");
                logge2.runLog();
            }
        };
        Thread t = new Thread(r);
        t.start();
        Thread t2 = new Thread(r2);
        t2.start();

 */

// strategy & decorator

 interface PaymentStrategy {
    void pay(int amount);
}

class creditCardPayment implements PaymentStrategy {
     @Override
     public void pay(int amount) {
         System.out.println("creditCardPayment..."+ amount);
     }
}

class upiPayment implements PaymentStrategy {
    @Override
    public void pay(int amount) {
        System.out.println("upiPayment..."+ amount);
    }
}

class shoppingFactory {
     private PaymentStrategy paymentStrategy;
     public shoppingFactory(PaymentStrategy paymentStrategy) {
         this.paymentStrategy = paymentStrategy;
     }
     void processPayment(int amount) {
         this.paymentStrategy.pay(amount);
     }
}

interface Coffee {
     String getDescription();
     double cost();
}

class SimpleCoffee implements Coffee {
     public String getDescription() {
         return "simple coffee";
     }

     public double cost() {
         return  5.62;
     }
}

class LatteCoffee implements Coffee {
    public String getDescription() {
        return "latte coffee";
    }

    public double cost() {
        return  9.12;
    }
}

abstract class CoffeeDecorator implements Coffee {
     protected Coffee decoratedCoffe;
     public CoffeeDecorator (Coffee coffee) {
         this.decoratedCoffe = coffee;
     }

     public String getDescription() {
         return decoratedCoffe.getDescription();
     }

     public double cost() {
         return decoratedCoffe.cost();
     }
}

class Milk extends CoffeeDecorator {
    public Milk(Coffee coffee) {
        super(coffee);
    }
    public String getDescription() {
        return super.getDescription() + "Milk";
    }

    public double cost() {
        return decoratedCoffe.cost() + 50.01;
    }
}

class Sugar extends CoffeeDecorator {
     public Sugar(Coffee coffee) {
         super(coffee);
     }
     public String getDescription() {
         return super.getDescription() + "Sugar";
     }

     public double cost() {
         return decoratedCoffe.cost() + 150.01;
     }
}

// builder pattern

class User {
     private String name;
     private int age;
     private String occupation;
     private Long money = 0L;

     public User() {

     }

    public User(String name){
        this.name = name;
    }

     User setName (String name) {
         this.name = name;
         return this;
     }

     User setAge(int age) {
         this.age = age;
         return this;
     }

     User setOccupation (String occupation) {
         this.occupation = occupation;
         return this;
     }

     User addMoney (long money) {
         this.money+= money;
         return this;
     }

     public User build() {
         return this;
     }

     public void printDetails() {
         System.out.println(this.name + ":" +this.age +":" + this.occupation + ":" +this.money);
     }
}

class Point {
     int x;
     int y;

     public Point(int x, int y) {
         this.x = x;
         this.y = y;
     }

     @Override
     public String toString() {
         System.out.println("{" + this.x + ", " + this.y + "}");
         return null;
     }

     @Override
     public boolean equals(Object o) {
         Point p  = (Point)o;
         return (this.x == p.x && this.y == p.y);
     }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}

class Factorial {

//     private Lock lock = new ReentrantLock();
     public boolean isZero(int n) {
         return n==0;
     }

     public int fact(int n) {
         if(isZero(n)) {
             return 1;
         }
         return n * fact(n - 1);
     }

     public int factTR(int n, int acc) {
         if (isZero(n)) {
             return acc;
         }
         return factTR(n -1, n*acc);
     }
}

class Fib implements Comparable{
     public int fib(int n) {
         if (n ==1 || n==0) {
             return n;
         }

         return fib(n-1) + fib(n-2);
     }

     public int fibN(int a, int b, int c) {
         if (a ==1 || a==0) {
             return b;
         }
         System.out.println(b);
         System.out.println(c);
         return fibN(a-1, c, c+b);
     }

     public int countChars(String ss, char c) {
         if(ss == null || ss.isEmpty()) {
             return 0;
         }
         if (ss.charAt(0) == c) {
             return 1 + countChars(ss.substring(1), c);
         }
         return countChars(ss.substring(1), c);
     }

    public int countChars(String ss, int len, char c, int count) {
        if(len == 0) {
            return count;
        }
        return countChars(ss, len-1, c, count + (ss.charAt(len) == c ? 1 : 0));
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

//    public String swapChars(String ss, int len) {
//         if(ss == null || ss.isEmpty()) {
//             return
//         }
//         if (ss.charAt(0) == 'a') {
//             ss.a
//         }
//
//
//    }
}

class Test implements FuncExm {

    @Override
    public int addSum(int a, int b) {
        return 0;
    }

    @Override
    public int mult(int a, int b) {
        return a*a*b*b;
    }
}

interface tcheck {
     int sum(int a, int b);
     default int mult(int a, int b) {
         return a*b;
     }
     public static boolean checkc(int a, int b) {
         return (a &b) == 0;
     }
}

interface tcheck2 {
    int sum(int a, int b);
    default int mult(int a, int b) {
        return a*a*b*b;
    }

    class AmbCheck implements tcheck, tcheck2 {
        @Override
        public int sum(int a, int b) {
            return a+b;
        }

        @Override
        public int mult(int a, int b) {
            tcheck.super.mult(a, b);
            tcheck2.super.mult(a, b);
            tcheck.checkc(1,2);
            return tcheck.super.mult(a, b);
        }
    }

    default public <T> T consumeAndCheck(Object in) {
       T t = null;

       Class<?> clazz = in.getClass();

       return t;
    }
}

class Counter {
     private int count = 1000;
     private final ReadWriteLock lock = new ReentrantReadWriteLock(true);
//     private Lock lock = new ReentrantLock();
     public void incCount() {
         count++;
     }

     public void decCount(int amount) {
         try {
             System.out.println(Thread.currentThread().getName() + " attempting taking lock ");
             if(lock.readLock().tryLock()) {
                 try {
                     if (count >= amount) {
                         Thread.sleep(3000);
                         System.out.println(Thread.currentThread().getName() + "withDraw " + amount);
                         System.out.println("Remaining " + count);
                         innerMethod(amount);
                     } else {
                         System.out.println("FAILED to decrease " + amount + "FROM " + count);
                     }
                 } finally {
                     lock.readLock().unlock();
                 }
             } else {
                 System.out.println( "CANNOT AQUIRE LOCK " + amount);
             }
         } catch (InterruptedException ie) {

         }
     }

     public void  innerMethod(int amount) {
         try {
             lock.writeLock().lock();
             System.out.println(Thread.currentThread().getName() + " aquired lock ");
             System.out.println("INNER");
             count-=amount;
         } finally {
             lock.writeLock().unlock();
             lock.writeLock().unlock();
         }
     }

     public int getCount() {
         return count;
     }
}

class ThreadCheck extends Thread {
     private Counter counter;
    public ThreadCheck(String name, Counter counter) {
        super(name);
        counter = counter;
    }

    @Override
    public void run() {
        for(int i = 1; i< 5; i++)
            this.counter.decCount(i*i);
//        try {
////            Thread.sleep(1000000L);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }
}

enum demo {
     MANGO, APPLE, ORANGE
}

class Producer implements Runnable {
     final private CyclicBarrier countDownLatch;
     public Producer(CyclicBarrier countDownLatch) {
         this.countDownLatch = countDownLatch;
     }
//
//     public Producer() {
//
//     }

     @Override
     public void run() {
         System.out.println(Math.random());
//         countDownLatch.countDown();
         try {
//             while(done())
//             wait();
//             notify();
             countDownLatch.await();
             System.out.println(Thread.currentThread().getName());
         } catch (InterruptedException e) {
             throw new RuntimeException(e);
         } catch (BrokenBarrierException e) {
             throw new RuntimeException(e);
         }
     }
}

interface tsf {
     public void add(int a, int b);

    default void sum() {
        sum();
    }
}

interface testcj {
     public void sum(int a, int b);

     default int adds() {
         return  1;
     }
}

interface testcj1 {
    public void sum(int a, int b);
    default int adds() {
        return  1;
    }
}

class op {
      private String name = "90";
}

class uid  extends op implements testcj, testcj1{

     public uid () {
         super();
     }

    @Override
    public void sum(int a, int b) {
        System.out.println(Thread.currentThread().getName() + " sum " + a + " ");
    }

    @Override
    public int adds() {
        return testcj.super.adds();
    }
}



public class Main {

     int addSum(int a, int b) {
         checkNonNumm();
         return a+b;
     }
    static void checNonNumm() {
//        addSum();
    }

    static void checkNonNumm() {
        checNonNumm();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, ClassNotFoundException {
        List<Integer> arr = new ArrayList<>(Arrays.asList(1,2,3,3,5,5,6,7,7,8,8,9,9));
        Set<Integer> ss = new HashSet<>();
        uid u = new uid();
//        System.out.println(u.name);

//        Enumeration e = arr.get().
//        User i = new User().setAge(0)

//        Reflection.getCallerClass();
//        Class.forName("com.mysql.jdbc.driver");
//        Thread.currentThread().getClass().getClassLoader().loadClass()
//        for(int el: arr) {
//            if(ss.contains(el)) {
////                return el;
//                System.out.println("FIrst duplicate:" + el);
//                return;
//            }
//            ss.add(el);
//        }

//        User u = new User().setAge()
//            Thread t = new Thread(() -> System.out.println("WWW"));
//            t.join();
//         ExecutorService es = Executors.newFixedThreadPool(20);
//        CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> {
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
////            return "Working";
//            return 70;
//        });
//        CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(() -> {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
////            return "Working";
//            return 10;
//        });
//        CompletableFuture<?> cf = null;
//        CompletableFuture<Void> cf3 =  CompletableFuture.allOf(cf, cf2);

        int[] arrs = {2,3,121,4,50,6,7};

        List<Integer>as = Arrays.asList(2,3,121,4,50,6,7);
        List<Integer> res = Arrays.stream(arrs)
                .boxed()
                .sorted(Comparator.reverseOrder())
                        .limit(2)
                                .collect(Collectors.toList());
//                .skip(1)
//                .findFirst();
        System.out.println(res);

//         cf.orTimeout(300, TimeUnit.MILLISECONDS);
//     System.out.println(cf.thenApply(x -> x*x).get());
//         System.out.println("MAIN");


//         Thread t = new Thread(() -> System.out.println(90));
//         t.start();
//         t.
//        ExecutorService es = Executors.newFixedThreadPool(5);
//        es.invokeAll()
//        CountDownLatch countDownLatch = new CountDownLatch(2);
//// After count reaches zero, this latch is spent — it cannot be reset.
//// If you need to wait again, you'll have to create a new CountDownLatch.
//
//        // waits for all threads to reach a common barier points
//        CyclicBarrier barrier = new CyclicBarrier(3, () -> System.out.println("DONE WITH ALL"));
//        es.submit(new Producer(barrier));
//        es.submit(new Producer(barrier));
//        es.submit(new Producer(barrier));
//
//        System.out.println("DONE" + barrier.getParties() + barrier.getNumberWaiting());

//        barrier.reset();
//        LinkedBlockingQueue<Runnable> i = new LinkedBlockingQueue<>();
//        ThreadPoolExecutor tp = new ThreadPoolExecutor(2, 5, 20, TimeUnit.MILLISECONDS, i);
//        countDownLatch.await();
//
//        tp.prestartCoreThread();
//
//        System.out.println("DONE");

//         ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//         scheduler.schedule(() -> System.out.println("SUBITT after 5 seconds"), 1, TimeUnit.SECONDS);
//
//         scheduler.scheduleAtFixedRate(() -> System.out.println("Running at fixed interval"),
//                 2, 7, TimeUnit.SECONDS);
//         scheduler.scheduleWithFixedDelay(() -> System.out.println("SUBITT after 5 seconds"), 1, 2, TimeUnit.SECONDS);
//
//         scheduler.schedule(() -> {
//             System.out.println("INITIATING SHUTDOWN");
//             scheduler.shutdownNow();
//
//         }, 20, TimeUnit.SECONDS);

//         ExecutorService es = Executors.newFixedThreadPool(1);
//         Callable<Integer> cs = () -> {
//             Thread.sleep(10000);
//    return             120;
//         };
//        Callable<Integer> cs1 = () ->      {        Thread.sleep(10000);
//        return             80;};
//        Callable<Integer> c2 = () -> {        Thread.sleep(10000);
//            return             49;};
//
//
//
//        List<Callable<Integer>> css = Arrays.asList(cs, cs1, c2);
//        Integer s = es.invokeAny(css); // returns one completed tasks, others are just cancelled
//
//        List<Future<Integer>> o = es.invokeAll(css, 1, TimeUnit.MILLISECONDS); // blocks until all are completed
//        System.out.println(o);
//        for(Future<Integer> f : o) {
//            System.out.println(f.get());
//        }
//        o.get(0).get(12, TimeUnit.MILLISECONDS);
//        System.out.println("F");
//         ExecutorService executorService = Executors.newFixedThreadPool(10);
//         for (int i = 0; i < 10; i++) {
//             int finalI = i;
//             Future<?> ress = executorService.submit(() -> {
//                 try {
//                     Thread.sleep(1000);
//                 } catch (InterruptedException e) {
//                     throw new RuntimeException(e);
//                 }
//                 int res = addSum(finalI, finalI*finalI);
//                 System.out.println(Thread.currentThread().getName() + ": " + res);
//            }, 90);
//             System.out.println(ress.get());
//         }
//
//         executorService.shutdown();
//        executorService.isTerminated();
//         executorService.shutdownNow();
//         executorService.awaitTermination(5, TimeUnit.SECONDS);
//         System.out.println(Thread.currentThread().getName() + ": done");
//        float f = .7f;
//        System.out.println(f);
        Map<Integer, Set<Integer>> mp = new HashMap<>();
//
        mp.computeIfAbsent(1, k -> new HashSet<>()).add(2);
//        addSum(20, 90);
//            System.gc();
//            demo [] dem = demo.values();
//            for(demo de: dem) {
//                System.out.println(de.name());
//            }
//            System.out.println(demo.valueOf("APPLE"));

//            Counter counter = new Counter();

//          ThreadCheck t1 = new ThreadCheck("SBI", counter);
//        ThreadCheck t2 = new ThreadCheck("PNB", counter);
//        t1.start();
//        t2.start();

//            new Thread().setDaemon(true);
//        new Thread().setPriority(Thread.MIN_PRIORITY);

//        Integer [] arr = {1,2,3,3,4,5,6};
//         boolean res = Arrays.stream(arr).distinct().count() == arr.length;
//         System.out.println(res);
//         List<Integer> ss = new ArrayList<>(Arrays.asList(arr));
//    Factorial factorial = new Factorial();
//    long initialTime = System.currentTimeMillis();
////        System.out.println(factorial.fact(10));
//        System.out.println(System.currentTimeMillis() - initialTime);
//        long initialTime2 = System.currentTimeMillis();
//        System.out.println(factorial.factTR(10, 1));
//        System.out.println(System.currentTimeMillis() - initialTime2);

//        Fib fib = new Fib();
////        System.out.println(fib.fib(10));
////        System.out.println(fib.fibN(10, 0, 1));
//        System.out.println(fib.countChars("thebigfoxjomped", 'o'));
//        System.out.println(fib.countChars("thebigfoxjomped", "thebigfoxjomped".length() - 1, 'o', 0));

//        Set<Point> st = new HashSet<>();
//        st.add(new Point(1,2));
//        st.add(new Point(1,2));
//        st.add(new Point(4,2));
//        System.out.println(st.size());
        User user = new User()
                .setAge(20)
                .setName("Testing")
                .addMoney(20000)
                .build();
        user.printDetails();

//        Bike b = new Bike("SUZUKI");
//        Bike bc = new Bike(null);
//        Bike bcd = new Bike("KTM");
//        List<Bike> bb = new ArrayList<>(Arrays.asList(b,bc,bcd));
//
//        bb.sort(Comparator.comparing(Bike::getName));
//        System.out.println(bb.get(0).getName());
//        System.out.println(bb.get(1).getName());
//        System.out.println(bb.get(2).getName());
//        List<String> ssc = bb.stream()
//                .map(bike -> b.getName())
//                .filter(name -> name.length() > 4)
//                .collect(Collectors.toList());
//        Optional<String> er = bb.stream()
//                .filter(bca-> bca.name.length()>2)
//                .map(each-> each.name)
//                .findFirst();
//        Optional<String> sv = Optional.ofNullable(bb.get(1).getName());
//
//        String efg = er.orElse("Random");

//        System.out.println(bb.get(1).getName());
//        try {
//            InputStream io = Files.newInputStream(Paths.get("C:/Users/Innostax-19-5-1/untitled/test.txt"));
//            byte[] bytes = new byte[1024];
//            int x;
//            System.out.println(io.available());
//            while (((x = io.read(bytes)) != -1)) {
//                System.out.println(new String(bytes, 0, x));
//            }
//        } catch (IOException ie) {
//            System.out.println(ie);
//        }

//        ArrayBlockingQueue<String> bq = new ArrayBlockingQueue<>(1);
//        upiPayment upiPayment = new upiPayment();
//        shoppingFactory shoppingFactory = new shoppingFactory(upiPayment);
//        shoppingFactory.processPayment(101213);
//
//        shoppingFactory = new shoppingFactory(new creditCardPayment());
//        shoppingFactory.processPayment(9999);

//        Coffee coffee = new SimpleCoffee();
//        System.out.println(coffee.getDescription());
//        System.out.println(coffee.cost());
//
//        coffee = new Milk(coffee);
//        System.out.println(coffee.getDescription());
//        System.out.println(coffee.cost());
//
//        coffee = new Sugar(coffee);
//        System.out.println(coffee.getDescription());
//        System.out.println(coffee.cost());
//        List<Integer> iList = Arrays.asList(1,2,3,4,56,78,85,45,45,43,24,3531,1212);
//        Map<Integer, Integer> finalIlist = iList.stream().parallel()
//                .filter(g-> g >50)
//                .map(s-> s*s)
//                .collect(Collectors.toMap(f-> f, f->f*f));
//        System.out.println(finalIlist);
//        int a, b = 15;
//        FuncExm ob = (aa, bb) -> {
////            a = 230; // by default all local variables inside a method are final
//            return aa+bb;
//        };
//        int aac = ob.addSum(15, 200);
//        System.out.println(aac);
//        System.out.println(ob.mult(10, 10));
//
//        Test t = new Test();
//        System.out.println(t.mult(10, 10));

//        int [] arr = new int[] {1,2,3,4,5};
        List<Integer> ls = Arrays.asList(1,2,3,4,55,5, 1,3,3);
        ls.add(1212);
//        List<Integer> ss = ls.parallelStream().map(a-> a*a).limit(10).skip(1).filter(b -> b<1).collect(Collectors.toList());
//
//        List<List<Integer>> listOf = Arrays.asList(Arrays.asList(1,2, 3, 4, 5), Arrays.asList(6, 7, 8, 9, 10));
//
//        Stream<List<Integer>> ups = listOf.stream();
//
//        Stream<Integer> fs = ups.flatMap(each -> each.stream());
//
//
//
//        Arrays.stream(arr).sum();
////        Streams.of(arr);
//        Stream.of(arr);

        Stream.generate(() -> new Random().nextInt()).limit(100).forEach(ac -> System.out.println(ac));
//        System.out.println("ADDA");
        Stream.iterate(12, a-> new Random().nextInt()).limit(12).forEach(ac -> System.out.println(ac));
        System.out.println(ls.parallelStream().distinct().sorted().collect(Collectors.groupingBy(each -> each >2)));
//        Optional<String> ac = Optional.of("ac");
//        ac.orElse();
    }

    public String imageToBase64String(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            imageString = "data:image/" + type + ";base64," + Base64.getEncoder().encodeToString(imageBytes);

            bos.close();
        } catch (IOException e) {
            LOGGER.info("Error converting the buffered image to string : ");
        }
        return imageString;
    }

//    public String byteToHex(byte[]arr) {
//        String res;
//
//
//
//        return res;
//    }

//    public BufferedImage generateQRCodeImage(String qrCodeText) throws Exception {
//        QRCodeWriter barcodeWriter = new QRCodeWriter();
//        Map<EncodeHintType, Object> hintMap = new HashMap<EncodeHintType, Object>();
//        hintMap.put(EncodeHintType.MARGIN, new Integer(0));
//        BitMatrix bitMatrix = barcodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, 400, 400, hintMap);
//
//        return MatrixToImageWriter.toBufferedImage(bitMatrix);
//    }

//    String readFile(String filename) throws IOException{
//        StringBuilder result = new StringBuilder();
//        try {
////            File file = File.
//            BufferedReader bis = new BufferedReader(new FileReader(""));
//            byte[] b = new byte[1024];
//            while (bis.read() > 0) {
//                result.append((char)bis.read());
//            }
//            bis.close();
//        } catch (IOException ie) {
//            System.out.println(ie);
//            throw ie;
//        }
//
//        return result.toString();
//    }

    static List<String> subarraySum(int[] arr, int target) {
        int size = arr.length;
        if (size ==1 || size ==0) {
//            return size;
        }
        int pt = -1;
        int st = 0; int ls = size-1;
        int lsum = arr[0]; int rsum=arr[ls];
        int mid = st;
        while(st<ls) {
            if (lsum == rsum) {
                pt = ls;
                break;
            }

            if (lsum> rsum) {
                ls--;
                rsum+=arr[ls];
                mid = ls;
            }else {
                st++;
                lsum+=arr[st];
                mid = st+1;
            }
        }

        System.out.println("pt = " + pt);
//        return  null;
        ArrayList<String> arrs = new ArrayList<>();
        return Collections.synchronizedList(arrs);
    }
}