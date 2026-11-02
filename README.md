# Janji
Saya Afit Fajar Rianto dengan NIM 2501826 mengerjakan Tugas Praktikum 6 pada Mata Kuliah Desain dan Pemrograman Berorientasi Objek (DPBO) untuk keberkahan-Nya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin

---

# Flappy Bird 2D (Java Swing)

## Deskripsi Program
Program ini merupakan sebuah aplikasi permainan interaktif 2 dimensi (2D) bertema **Flappy Bird** berbasis desktop. Aplikasi ini dikembangkan sebagai pemenuhan Tugas Praktikum 6 mata kuliah Desain dan Pemrograman Berorientasi Objek (DPBO). 

### Teknologi dan Pustaka yang Digunakan:
- **Bahasa Pemrograman:** Java (JDK 21)
- **GUI Framework:** **Java Swing** (`JFrame`, `JPanel`, `JButton`, `JLabel`, `CardLayout`, `GridBagLayout`) untuk membangun antarmuka visual pengguna, sistem tata letak (layout), dan komponen interaktif.
- **Grafika & Rendering:** **Java AWT** (`Graphics`, `Color`, `Font`, `FontMetrics`, `Image`, `Dimension`, `Insets`) untuk manipulasi grafis tingkat kanvas, rendering gambar latar dan *sprites*, serta pewarnaan dan tipografi antarmuka.
- **Event Handling:** `java.awt.event.ActionListener` dan `java.awt.event.KeyListener` untuk menangkap input aksi tombol serta input keyboard pemain secara *real-time*.
- **Game Engine Timers:** `javax.swing.Timer` untuk mengatur pembaruan frame permainan (*game loop*) dan interval waktu kemunculan rintangan pipa (*spawner*).

---

## 1. Desain Program

Program ini dirancang dengan menerapkan prinsip dasar **Object-Oriented Programming (OOP)** seperti modularitas, enkapsulasi, dan pemisahan tanggung jawab (*Separation of Concerns*). Struktur kode memisahkan lapisan logika permainan (*Logic/Model*), antarmuka grafis (*View*), objek-objek entitas dalam dunia game (*Player* dan *Pipe*), serta pengatur jalannya program utama (*App*).

```
                      +-------------------+
                      |      App.java     |
                      |  (Main & Window)  |
                      +---------+---------+
                                |
                +---------------+---------------+
                |                               |
                v                               v
        +---------------+               +---------------+
        |   Menu Panel  |               |   View.java   |
        |  (Start/Exit) |               | (Canvas/GUI)  |
        +---------------+               +-------+-------+
                                                |
                                                v
                                        +---------------+
                                        |   Logic.java  |
                                        | (Game Engine) |
                                        +-------+-------+
                                                |
                                +---------------+---------------+
                                |                               |
                                v                               v
                        +---------------+               +---------------+
                        |  Player.java  |               |   Pipe.java   |
                        | (Bird Entity) |               | (Obstacle)    |
                        +---------------+               +---------------+
```

### Struktur Kelas & Komponen Secara Lengkap:

1. **`App.java` (Inisialisasi Aplikasi & Navigasi Layar)**
   - **Peran & Tanggung Jawab:** Kelas utama (*entry point*) yang bertindak sebagai inisiator aplikasi dan koordinator navigasi antarmuka.
   - **Komponen & Cara Kerja:**
     - Membuat jendela utama aplikasi menggunakan `JFrame` dengan judul "FlappyBird", ukuran kanvas tetap $360 \times 640$ piksel, pemosisian di tengah layar (`setLocationRelativeTo(null)`), dan penonaktifan pengubahan ukuran jendela (`setResizable(false)`).
     - Menggunakan kontainer `JPanel` utama dengan manajer tata letak **`CardLayout`** yang memungkinkan pergantian kartu antarmuka secara dinamis antara dua layar:
       1. **Layar Menu Utama (`MENU`):** Panel pembuka yang didekorasi dengan gambar latar belakang *Flappy Bird*. Menggunakan `GridBagLayout` untuk menata elemen secara proporsional di tengah, terdiri dari:
          - `JLabel`: Judul permainan bergaya tebal berukuran besar ("Flappy Bird").
          - `JButton` ("Start Game"): Tombol yang dilengkapi *action listener* untuk beralih ke layar game (`GAME`), memberikan fokus input keyboard ke kanvas permainan, dan memulai logika permainan melalui `logic.startGame()`.
          - `JButton` ("Exit"): Tombol untuk keluar dan menutup program menggunakan `System.exit(0)`.
       2. **Layar Permainan (`GAME`):** Panel permainan interaktif yang menggunakan instance dari kelas `View`.

2. **`Player.java` (Entitas Burung Pemain)**
   - **Peran & Tanggung Jawab:** Merepresentasikan objek burung yang dikendalikan oleh pemain, memuat seluruh data fisik, posisi, ukuran, serta representasi visual burung.
   - **Atribut & Enkapsulasi:**
     - `posX`, `posY`: Koordinat horizontal dan vertikal burung pada layar.
     - `width`, `height`: Dimensi fisik burung ($34 \times 24$ piksel) yang juga digunakan dalam perhitungan kotak pembatas (*bounding box*) tabrakan.
     - `image`: Objek `Image` yang memuat gambar sprite karakter burung (`bird.png`).
     - `velocityY`: Kecepatan pergerakan vertikal burung. Nilai positif menggerakkan burung turun akibat gravitasi, sedangkan nilai negatif menggerakkan burung naik saat pemain melompat.
     - Menyediakan metode *getter* dan *setter* lengkap untuk setiap atribut agar kelas pengelola logika dapat memanipulasi posisi serta kecepatan burung secara aman dan terkontrol.

3. **`Pipe.java` (Entitas Rintangan Pipa)**
   - **Peran & Tanggung Jawab:** Merepresentasikan rintangan pipa vertikal yang bergerak melintasi layar dari arah kanan ke kiri.
   - **Atribut & Enkapsulasi:**
     - `posX`, `posY`: Koordinat pipa pada kanvas.
     - `width`, `height`: Dimensi pipa ($64 \times 512$ piksel) untuk rendering dan pemeriksaan batas tabrakan.
     - `image`: Referensi gambar sprite pipa, baik pipa atas (`upperPipe.png`) maupun pipa bawah (`lowerPipe.png`).
     - `velocityX`: Kecepatan pergerakan horizontal pipa ke arah kiri.
     - `passed`: Nilai boolean penanda (*flag*). Berfungsi untuk memastikan bahwa penambahan skor hanya dihitung satu kali ketika burung berhasil melewati sepasang pipa tersebut.
     - Menyediakan metode *getter* dan *setter* untuk posisi, dimensi, gambar, serta status kelulusan (`isPassed()` / `setPassed()`).

4. **`Logic.java` (Mesin Permainan, Simulasi Fisika & Input Handling)**
   - **Peran & Tanggung Jawab:** Berperan sebagai pengendali logika utama (*Controller/Game Engine*) yang mengelola siklus waktu (*game loop*), simulasi pergerakan fisika, generator rintangan, deteksi benturan, pembaruan skor, serta penanganan input keyboard dari pemain.
   - **Komponen & Mekanisme Internal:**
     - **Manajemen Timer:**
       - `gameLoop` (`javax.swing.Timer`): Dijalankan setiap $\approx 16.6$ milidetik (ekuivalen dengan 60 FPS). Setiap kali timer ini berdetak, metode `actionPerformed()` memanggil metode `move()` untuk memperbarui seluruh kalkulasi posisi dan kemudian meminta `View` untuk merender ulang kanvas melalui `view.repaint()`.
       - `pipesCooldown` (`javax.swing.Timer`): Dijalankan dengan interval 1500 milidetik (1.5 detik) untuk mengeksekusi metode `placePipes()`, yaitu membangkitkan sepasang pipa baru (pipa atas dan bawah) dengan posisi bukaan celah vertikal yang diacak secara proporsional.
     - **Simulasi Fisika (`move()`):**
       - Menerapkan gravitasi konstan (`gravity = 1`) pada `velocityY` burung di setiap siklus.
       - Membatasi pergerakan burung agar tidak menembus batas atas kanvas (`Math.max(posY, 0)`).
       - Menggeser posisi semua pipa aktif ke arah kiri sebesar `pipeVelocityX = -2`.
     - **Deteksi Tabrakan (*AABB Collision Detection*):**
       - Metode `checkCollision(Player p, Pipe pipe)` melakukan pengujian tumpang tindih kotak pembatas (*Axis-Aligned Bounding Box*) antara koordinat persegi burung dengan koordinat persegi setiap pipa.
       - Memeriksa batas bawah kanvas: Jika posisi bawah burung menyentuh atau melewati tinggi kanvas (`player.getPosY() + player.getHeight() >= frameHeight`), maka kondisi Game Over langsung terpicu.
     - **Penghitungan Skor:**
       - Mengevaluasi apakah burung telah melewati sisi kanan pipa atas yang belum pernah ditandai (`!pipe.isPassed()`). Jika terpenuhi, nilai `passed` diubah menjadi `true`, skor bertambah 1, dan `view.updateScore(score)` dipanggil untuk memperbarui tampilan label skor.
     - **Penanganan Input Keyboard (`KeyListener`):**
       - Tombol `SPACE`: Memberikan dorongan kecepatan ke atas (`velocityY = -9`) agar burung melompat, hanya aktif saat permainan sedang berjalan (tidak game over).
       - Tombol `R`: Melakukan restart permainan saat kondisi game over melalui pemanggilan `restartGame()`.
     - **State Game Over & Restart:**
       - Saat `gameOver == true`, kedua timer (`gameLoop` dan `pipesCooldown`) dihentikan agar seluruh animasi berhenti.
       - Metode `restartGame()` mereset posisi burung ke posisi awal, menolkan kecepatan, mengosongkan daftar pipa, mengembalikan skor ke angka 0, memperbarui label skor, dan mengaktifkan kembali kedua timer.

5. **`View.java` (Lapisan Tampilan Grafis & Antarmuka Permainan)**
   - **Peran & Tanggung Jawab:** Kelas turunan `JPanel` yang bertindak sebagai kanvas visual untuk merender semua aset grafis permainan ke layar, menampilkan status skor secara konstan, serta menampilkan informasi status ketika permainan berakhir.
   - **Komponen & Mekanisme Internal:**
     - **`scoreLabel` (`JLabel`):** Komponen teks Swing yang ditempatkan secara absolut di pojok kiri atas kanvas (posisi $X: 20, Y: 20$) dengan gaya huruf tebal (bold, ukuran 24) berwarna putih, yang bertugas menyajikan informasi skor terkini secara jelas kepada pemain.
     - **Metode `paintComponent(Graphics g)` & `draw(Graphics g)`:**
       1. Menggambar gambar latar belakang langit/kota (`background.png`) menutupi seluruh ukuran panel ($360 \times 640$ piksel).
       2. Merender sprite burung sesuai koordinat yang diperoleh dari kelas `Logic`.
       3. Melakukan iterasi dan merender setiap pipa (pipa atas dan bawah) yang sedang berada di dalam daftar pipa aktif.
       4. **Tampilan Game Over Overlay:** Jika status permainan dalam keadaan game over, `View` menggambar lapisan kotak semi-transparan hitam (`new Color(0, 0, 0, 150)`) di atas kanvas, lalu menampilkan teks tebal besar bertuliskan **"GAME OVER"** dan teks instruksi **"Press 'R' to Restart"** tepat di tengah layar menggunakan perhitungan presisi `FontMetrics`.
     - **Integrasi Input:** Mengaktifkan kapabilitas fokus (`setFocusable(true)`) dan mendaftarkan kelas `Logic` sebagai *listener* keyboard pada panel permainan.

---

## 2. Penjelasan Alur Program

### A. Alur Memulai Permainan (Main Menu)
1. Program pertama kali dijalankan melalui metode `main()` pada kelas `App.java` di dalam thread *Event Dispatch Thread (EDT)* Swing.
2. Jendela utama terbuka dan menampilkan **Main Menu** berlatar belakang dunia Flappy Bird dengan judul dan dua tombol pilihan:
   - **Start Game**: Mengalihkan kartu tampilan dari `MENU` ke `GAME` pada `CardLayout`, memberikan fokus masukan keyboard ke panel permainan `View`, dan memanggil fungsi `logic.startGame()` untuk memulai permainan.
   - **Exit**: Keluar dari aplikasi sepenuhnya melalui instruksi `System.exit(0)`.

### B. Alur Permainan Berjalan (Gameplay Loop)
1. **Gravitasi & Pergerakan:**
   - Setiap detak dari `gameLoop` (sekitar 60 kali per detik), nilai `gravity` diakumulasikan ke kecepatan vertikal burung (`velocityY`), sehingga burung mengalami percepatan jatuh bebas ke bawah.
   - Pemain dapat menekan tombol `SPACE` pada keyboard untuk memberikan impuls ke atas secara instan (`velocityY = -9`), membuat burung mengepakkan sayap dan terbang melambung.
2. **Pergerakan Rintangan:**
   - Seluruh pipa yang terdapat di dalam koleksi `pipes` digeser ke arah kiri secara linier sesuai kecepatan horizontal `pipeVelocityX`.
   - Timer `pipesCooldown` secara periodik setiap 1.5 detik memanggil metode `placePipes()` untuk mengkalkulasi ketinggian acak dan menaruh sepasang pipa baru (pipa atas dan pipa bawah) di sisi kanan layar.
3. **Penghitungan Skor:**
   - Ketika koordinat horizontal burung telah melampaui ujung kanan pipa atas yang berstatus belum dilewati (`!pipe.isPassed()`), status pipa ditandai menjadi dilewati (`pipe.setPassed(true)`).
   - Nilai variabel `score` bertambah satu (+1), dan fungsi `view.updateScore(score)` langsung memperbarui teks pada `JLabel scoreLabel` di sudut kiri atas layar.

### C. Alur Game Over & Restart
1. **Pemicu Kondisi Kalah:**
   - Burung menabrak salah satu bagian pipa rintangan (diperiksa melalui fungsi `checkCollision()`), **ATAU**
   - Burung terjatuh hingga menyentuh batas bawah area jendela permainan (`posY + height >= frameHeight`).
2. **Aktivasi Status Game Over:**
   - Variabel status `gameOver` pada kelas `Logic` diset menjadi `true`.
   - Timer `gameLoop` dan `pipesCooldown` seketika dihentikan (`stop()`), membekukan pergerakan seluruh objek di layar.
   - Kanvas `View` merender lapisan gelap transparan serta menyajikan teks **"GAME OVER"** dan **"Press 'R' to Restart"** di bagian tengah layar.
3. **Mekanisme Restart Permainan:**
   - Saat status game over aktif, penekanan tombol `R` pada keyboard akan memicu pemanggilan metode `restartGame()`.
   - Seluruh status permainan dikembalikan ke kondisi awal: burung diposisikan kembali ke titik awal dengan kecepatan nol, daftar pipa dikosongkan, skor direset ke nilai 0, teks pada `JLabel` diperbarui kembali ke `"Score: 0"`, dan kedua timer dinyalakan kembali untuk memulai sesi permainan yang baru.
