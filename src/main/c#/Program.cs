// =======================================================================
//    QSH_Example (c) 2013 Nikolay Moroshkin, http://www.moroshkin.com/
// =======================================================================

/*************************************************************************
Программа  предназначена  для  демонстрации  работы  с классами читателя и
писателя  файлов  в  формате  QSH. Она выводит информацию о файле, который
задан  в  первом  параметре  командной  строки.  Затем она последовательно
читает  все  данные  из  этого  файла  и  записывает их в другой файл, имя
которого   задается   во  втором  параметре  командной  строки.  При  этом
корректируется  заголовок нового файла: обновляется имя программы-писателя
и  комментарий,  который  может  быть  задан в третьем параметре командной
строки.
*************************************************************************/


using System;
using System.IO;

using QScalp;
using QScalp.History.Reader;
using QScalp.History.Writer;

namespace qsh_example
{
  static class Program
  {
    // **********************************************************************

    const string appName = "QSH_Example";

    // Экземпляр класса читателя. Через эту переменную будем получать все
    // данные из .qsh файла.
    static QshReader qr;

    // Экземпляр класса писателя. Через эту переменную будем записывать все
    // данные в новый .qsh файл.
    static QshWriter qw;

    // Массив для классов потоков писателя. Он потребуется в процессе записи
    // нового файла. Т.к. каждому потоку соответствует свой класс, массив
    // будет общего типа, потом будем преобразовывать по мере надобности.
    static object[] streams;

    // **********************************************************************

    static void Main(string[] args)
    {
      Console.WriteLine("{0} (c) 2013 Nikolay Moroshkin, http://www.qscalp.ru", appName);
      Console.WriteLine();

      // Проверим аргументы командной строки.

      if(args.Length == 0)
      {
        Console.WriteLine("Usage: {0} input_file [output_file] [new_comment]", appName.ToLower());
        return;
      }

      // Методы чтения и записи могут вызвать исключение, делаем
      // соответствующую обработку.

      try
      {
        // Создаем класс читателя (его нельзя создать напрямую через
        // конструктор) и открываем исходный файл.

        qr = QshReader.Open(args[0]);

        // Выведем информацию об исходном файле.

        Console.WriteLine("File name: " + Path.GetFileName(args[0]));
        Console.WriteLine("File size: " + (qr.FileSize / 1024.0).ToString("F1") + " KBytes");
        Console.WriteLine("Recorder name: " + qr.AppName);
        Console.WriteLine("Record time: " + qr.RecDateTime.ToString("dd/MM/yyyy HH:mm:ss.fff"));
        Console.WriteLine("User comment: " + qr.Comment);
        Console.WriteLine();

        // Выведем информацию о потоках.

        Console.WriteLine("Streams ({0}):", qr.StreamsCount);

        for(int i = 0; i < qr.StreamsCount; i++)
        {
          Console.Write("  " + i + "\t" + qr[i].Type);

          ISecurityStream ss = qr[i] as ISecurityStream;

          if(ss == null)
            Console.WriteLine();
          else
            Console.WriteLine("\t" + ss.Security);
        }

        // Если не задан второй параметр командной строки, закончим работу.

        if(args.Length < 2)
        {
          qr.Dispose();
          return;
        }

        // Подготовим новое имя писателя и комментарий, если указан
        // (для красоты и примера).

        string newAppName = qr.AppName.EndsWith(appName) ? qr.AppName : (qr.AppName + " + " + appName);
        string newComment = args.Length >= 3 ? args[2] : qr.Comment;

        // Создадим класс писателя и выходной файл. В конструкторе укажем имя
        // файла, имя писателя и комментарий. Время файла и количество потоков
        // возьмем из исходного файла.

        qw = new QshWriter(args[1], newAppName, newComment, qr.RecDateTime, qr.StreamsCount);

        // Теперь подготовим потоки для записи (streams) и обработчики данных
        // для читателя.

        streams = new object[qr.StreamsCount];

        for(int i = 0; i < qr.StreamsCount; i++)
          switch(qr[i].Type)
          {
            case StreamType.Stock:
              {
                // Т.к. тип потока "Stock" смело преобразуем его в
                // соответствующий интерфейс.
                IStockStream s = (IStockStream)qr[i];

                // Добавляем обработчик читаемых данных. При этом если в
                // исходном файле несколько потоков со стаканом, они все
                // будут обрабатываться этим обработчиком. Различать исходные
                // потоки одного типа мы будем уже внутри обработчика.
                s.Handler += StockHandler;

                // Создадим соответствующий поток в выходном файле и сохраним
                // его в массиве.
                streams[i] = qw.CreateStockStream(s.Security);

                // Остальные потоки по аналогии. Обратите внимание, название
                // интерфейса и метода создания для каждого типа потока свои.
              }
              break;

            case StreamType.Deals:
              {
                IDealsStream s = (IDealsStream)qr[i];
                s.Handler += DealsHandler;
                streams[i] = qw.CreateDealsStream(s.Security);
              }
              break;

            case StreamType.Orders:
              {
                IOrdersStream s = (IOrdersStream)qr[i];
                s.Handler += OrdersHandler;
                streams[i] = qw.CreateOrdersStream(s.Security);
              }
              break;

            case StreamType.Trades:
              {
                ITradesStream s = (ITradesStream)qr[i];
                s.Handler += TradesHandler;
                streams[i] = qw.CreateTradesStream(s.Security);
              }
              break;

            case StreamType.Messages:
              ((IMessagesStream)qr[i]).Handler += MsgHandler;
              streams[i] = qw.CreateMessagesStream();
              break;

            case StreamType.AuxInfo:
              {
                IAuxInfoStream s = (IAuxInfoStream)qr[i];
                s.Handler += AuxInfoHandler;
                streams[i] = qw.CreateAuxInfoStream(s.Security);
              }
              break;

            default:
              // Если обнаружился тип потока, который мы не обрабатываем, то
              // просто выведем об этом предупреждение. Вообще, обрабатывать
              // можно только те потоки, которые нужны. 

              Console.WriteLine("Unknown stream type: " + qr[i].Type);
              break;
          }

        // Теперь читаем исходный файл. Сразу после открытия он готов к
        // чтению первого кадра: qr.CurrentDateTime и qr.CurrentStreamIndex
        // содержат соответствующие значения. Когда будет достигнут конец
        // файла, qr.CurrentDateTime будет содержать DateTime.MaxValue.

        Console.WriteLine();
        Console.WriteLine("Processing...");

        int lastPercent = 0;

        while(qr.CurrentDateTime != DateTime.MaxValue)
        {
          // Выведем, какой процент исходного файла уже обработали. Если
          // исходные файл сжат, то это значение может быть немного
          // некорректно (особенно для небольших файлов), т.к. сжатый файл
          // читается большими блоками.

          int percent = (int)(100 * qr.FilePosition / qr.FileSize);
          if(lastPercent != percent)
          {
            lastPercent = percent;
            Console.Write("\r{0}%", percent);
          }

          // Прочтем кадр исходного файла. Параметр push задает вызывать ли
          // при этом обработчик или нет. После возврата из метода свойства
          // qr.CurrentDateTime и qr.CurrentStreamIndex будут содержать
          // значения для следующего кадра.
          qr.Read(true);
        }

        Console.WriteLine("\rDone.");
      }
      catch(Exception e)
      {
        Console.WriteLine("Error: " + e.Message);
      }

      // На этом все.

      if(qw != null)
        qw.Dispose();

      if(qr != null)
        qr.Dispose();

    }

    // **********************************************************************

    // Обработчики читаемой информации. В каждом из них мы просто передаем
    // полученные данные в поток писателя с тем же индексом, что и текущий
    // поток читателя.

    static void StockHandler(int skey, Quote[] quotes, Spread spread)
    {
      ((StockStream)streams[qr.CurrentStreamIndex]).Write(qr.CurrentDateTime, quotes);
    }

    static void DealsHandler(Deal deal)
    {
      ((DealsStream)streams[qr.CurrentStreamIndex]).Write(qr.CurrentDateTime, deal);
    }

    static void OrdersHandler(int skey, OwnOrder order)
    {
      ((OrdersStream)streams[qr.CurrentStreamIndex]).Write(qr.CurrentDateTime, order);
    }

    static void TradesHandler(int skey, TraderReply reply)
    {
      ((TradesStream)streams[qr.CurrentStreamIndex]).Write(qr.CurrentDateTime, reply);
    }

    static void MsgHandler(Message msg)
    {
      ((MessagesStream)streams[qr.CurrentStreamIndex]).Write(qr.CurrentDateTime, msg);
    }

    static void AuxInfoHandler(AuxInfo auxInfo)
    {
      ((AuxInfoStream)streams[qr.CurrentStreamIndex]).Write(qr.CurrentDateTime, auxInfo);
    }

    // **********************************************************************
  }
}
