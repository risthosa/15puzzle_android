**15 Puzzle**

Karışık halde bir tahta üzerine dizilmiş, üzerinde sayılar yazan taşları sayı sırasına göre boş kutuyu kaydırarak dizmeye çalıştığımız bir bulmaca oyunudur. 

Bu raporda tamamladığımız 15puzzle mobil uygulamasının son durumundan ve ayrıntılarından bahsedeceğiz.

**Mobil Uygulamanın Son Durumu**

15puzzle mobil uygulamasında yapılması planlananlardan aşağıdakiler tamamlandı.

- Üç zorluk seviyesi olan 8, 15 ve 24 taşla oynama opsiyonu.
- Taşların kaydırma animasyonları ve oyun sonu animasyonu.
- Oyun süresini tutan kronometre ve hamle sayacı.
- Taşların sıralanabilir kombinasyonlarını üreten bir algoritma.
- Yüksek skorları tutan bir veri tabanı ve skor menüsü.
- Oyunu çözebilen bir algoritma.

**Yapılan Güncellemeler**

•Taşları otomatik olarak sıralayan eklenti için karıştırma algoritması değiştirildi. Rastgele bir kombinasyon üretmek yerine yeni uygulamada kontrollü bir karıştırma işlemi yapılıyor.

•Karıştırma işlemi yapılırken yapılan işlemler kayıt ediliyor ve çöz butonuna basıldığında karıştırma işlemi tersine doğru yürütülerek tahta sıralı hale getiriliyor

•Bir veri tabanı eklendi ve en kısa süreyle en az hamle skorları oyun tahtasının üzerinde, her boyut için ayrı yazdırılıyor.

**Mobil Uygulamanın Tasarım Ayrıntıları**

Geliştirdiğimiz mobil uygulama dört sınıftan ve arayüz bileşenlerinden oluşmakta. Bunlardan ayrıntılarıyla bahsedeceğiz. 

**MainActivity Sınıfı**

MainActivity sınıfı uygulamanın üzerinde çalıştığı, arayüz bileşenlerinin tanımlanıp düzenlendiği, animasyonların ve tıklama görevlerinin gerçekleştirildiği sınıftır. MainActivity sınıfına ait özel fonksiyonlar bulunmaktadır; bunlar onCreate onStart ve onRestart fonksiyonlarıdır ve ebeveyn sınıfından çağrılırlar. Ayrıca taşların konum hesaplamalarının yapılarak yerlerinin değiştirildiği onClick fonksiyonu da bu sınıfta bulunur.

onCreate fonksiyonu program çalıştırıldığında çağrılır ve gövdesinde bazı arayüz nesneleri çağrılmış ve bu arayüz nesnelerinin animasyon seçenekleri ayarlanmıştır.  

onStart fonksiyonu onCreate fonksiyonunun ardından çalışır ve burada Combination sınıfından mevcut oyunun taş sıralamasını yapacak fonksiyon çağrılır. Ardından döngü içinde arayüze eklenecek taşların stil ayarları yapılır ve taşlar oyun tahtasına dinamik olarak eklenir. Dinamik olarak eklenmesinin sebebi farklı zorluk seviyelerinde taşların boyut ve sayısının değişmesinden ötürüdür.

![Aspose Words a2f76d2c-5c48-4bcc-9d6f-6ff63f120fe5 001](https://github.com/user-attachments/assets/7dc99722-f5c5-47f6-b4d4-8f6afaba9cc6)

*Şekil 1: onStart fonksiyonunun içinde veri tabanından yüksek skorları getiren kod parçası*

Şekil1: Veri tabanından üç zorluk seviyesine ait yüksek skorlar getirilip oyun ekranında yazdırılır. Bu işlem her oyun başlangıcında yapılır.

onRestart fonksiyonu ise oyun alt sekmeye alındığında veya ekran kilitlenip tekrar açılınca çalışır. Bu yüzden bu fonksiyonun içinde tahtadaki taşları silen ve süreyi sıfırlayan deyimler bulunur.

onClick fonksiyonu tahtadaki boş kutunun konumu ve tıklanan kutunun konumu arasındaki dikey ve yatay mesafeyi hesaplayarak kutuların yer değişmesinin mümkün olup olmadığını tespit ederek kaydırma işlemini gerçekleştirir.

Aynı zamanda taşların sıra kontrolü de onClick fonksiyonu içinde yapılır. Çünkü her hamleden sonra oyunun bitip bitmediği bilinmelidir. Eğer oyun bitmişse bir animasyon çalışır ve taşlar dikey eksende kayarak görünürden kaybolur. Ardından skorlar kayıt edilir.

![Aspose Words a2f76d2c-5c48-4bcc-9d6f-6ff63f120fe5 002](https://github.com/user-attachments/assets/e287b282-9433-4f37-af4a-2af21ee7f2a6)

*Şekil 2: onClick fonksiyonu içinde bir if bloğu*

Şekil2: Yukarıdaki şekil1’de tıklanan taşın boş kutuyla yatay düzlemde bir kutu mesafesinde olup dikey düzlemde aynı seviyede olup olmadıkları kontrol ediliyor. Eğer sonuç ‘true’ ise yatay düzlemde hangisinin diğerinden daha solda olduğu tespit edilip ona göre yer değiştirme işlemleri yapılıyor.

Bu fonksiyonlar dışında oyunda zorluk seviyesini değiştiren radioButton nesneleri bulunur. Bu butonların kontrol fonksiyonu  MainActivity sınıfında bulunur ve aşağıdaki gibidir.

![Aspose Words a2f76d2c-5c48-4bcc-9d6f-6ff63f120fe5 003](https://github.com/user-attachments/assets/81797e16-fce1-405f-90dd-450cc96291e3)

*Şekil 3: RadioButton nesneleri*

Şekil3: MainActivity sınıfı içinde bulunan onRadioButtonClicked isimli fonksiyon bir radioButton dinleyicisidir ve bu nesnelere tıklandığı zaman oyundaki tahta boyutu olan n değeri değişir ve ardından onStart fonksiyonuyla oyun tekrar çalıştırılır. Yeni n değeriyle tahta tekrar düzenlenir.

**DataBaseHelper Sınıfı**

Bu sınıfta SQLITE ile bir veri tabanı ve tablo oluşturulmuştur. Tablo kolonları n(oyun tahtasının boyutu), bestScore, bestTime olarak tanımlanmıştır. Sınıfın içerisinde en iyi skor ve süreyi kaydetmek için bir kayıt fonksiyonu bulunur. Veri tabanındaki en iyi süreyi ve skoru çağırmak için iki adet fonksiyon bulunur.

![Aspose Words a2f76d2c-5c48-4bcc-9d6f-6ff63f120fe5 004](https://github.com/user-attachments/assets/68d92b73-368a-4f7b-8e13-fcc40520400b)

*Şekil 4: Oyun ekranında bulunan yüksek skorlar*

Şekil4: Veri tabanında her zorluk seviyesi (3x3, 4x4, 5x5) için yapılmış en az hamle ve en düşük süre tutulur. Her oyun başlangıcında bu skorlar veri tabanından getirilerek oyun tahtasının üzerine yazdırılır. 

**Combination Sınıfı**

Bu sınıfta statik bir fonksiyon ve statik gameNums dizisi bulunur. Fonksiyon sıralı bir diziyi oyun kurallarına uygun şekilde karıştırır ve karıştırırken yaptığı yer değiştirme işlemlerini path adında bir diziye kaydeder. Path dizisindeki işlemlerin tersi uygulanırsa taşlar sıralı hale gelir. Bu dizi solve fonksiyonu içinde kullanılır.

**TimeAndCounter Sınıfı**

Zaman ve sayaç fonksiyonlarını içinde bulunduran sınıftır. Bu fonksiyonlar sayaç için sıfırla ve arttır olmak üzere iki tanedir. Kronometre için ise üç fonksiyon bulunur. Bunlar başlat sıfırla ve durdur fonksiyonudur. 

![Aspose Words a2f76d2c-5c48-4bcc-9d6f-6ff63f120fe5 005](https://github.com/user-attachments/assets/5d063695-c809-4ca1-9a35-d79687544fcc)

*Şekil 5: Oyun ekranında bulunan süre ve hamle sayacı.*

Şekil5: Kronometre ve sayaç birer arayüz nesnesidir ve bu TimeAndCounter nesnesine ait fonksiyonlar çalıştığında bu nesneler güncellenir. Örneğin onClick fonksiyonu çalıştığında TimeAndCounter sınıfına ait counterPlus fonksiyonu çalışır ve moves değişkeni bir artarak ekranda güncellenir.

**Otomatik Çözüm**

Oyunun ilk sürümünde tamamen rastgele şekilde düzenlenen oyun tahtası için bir çözüm algoritması geliştirdik fakat bu algoritmanın normal bir cep telefonunun kullanılabilir belleğinin çok üzerinde kaynak tükettiğini gördüğümüz için sezgisel yöntemlerden farklı bir metot ile otomatik çözüm özelliğini geliştirdik. 

Kullandığımız metot kısaca karıştırılma esnasında çözüm yolunu aklında tutuyor ve çöz butonuna basıldığında karıştırma işleminin tersini uyguluyor. Karıştırma işleminden sonra kullanıcı tarafından fazladan bir karıştırma yapılırsa bunlar da tutuluyor ve tersi uygulanıyor. 

**Arayüz**

Oyun kaynak kodu dosyasındaki activity\_main.xml isimli dosyada oyunun arayüz nesneleri ve görünümleri bulunur. Bu nesnelere ait özel görünümler hazırlanır ve bu dosyada çağrılır.

Oyun başlangıcında sadece boş bir tahta görünümü, sayaçlar ve zorluk seviyesi butonları görünür. Bunun sebebi tahtanın oyun başladığında dinamik olarak doldurulmasıdır. 

![Aspose Words a2f76d2c-5c48-4bcc-9d6f-6ff63f120fe5 006](https://github.com/user-attachments/assets/24c5aafd-ed4b-4d89-be10-f014ffd74f0f)

*Şekil 6: activity\_main.xml dosyasının UI görünümü*

Şekil6: Siyah dikdörtgen doldurulmamış oyun tahtası olmak üzere oyun ekranının henüz oyun çalıştırılmadan önceki hali.

Aşağıda ise farklı zorluk seviyesi için görünümler bulunmaktadır

![Aspose Words a2f76d2c-5c48-4bcc-9d6f-6ff63f120fe5 007](https://github.com/user-attachments/assets/40f39568-6370-4339-92cd-54eb6df67084)![Aspose Words a2f76d2c-5c48-4bcc-9d6f-6ff63f120fe5 008](https://github.com/user-attachments/assets/45e4e45c-8d43-44b3-af37-9c3fa4184402)![Aspose Words a2f76d2c-5c48-4bcc-9d6f-6ff63f120fe5 009](https://github.com/user-attachments/assets/f6f52a4a-66c7-4936-8c01-65e279b32ce9)

*3x3 4x4 5x5

![Aspose Words a2f76d2c-5c48-4bcc-9d6f-6ff63f120fe5 010](https://github.com/user-attachments/assets/ac1b899d-0ff1-4451-9a62-eebdd50e3766)

*Şekil 7: Oyun sonu tahtanın görünümü*
