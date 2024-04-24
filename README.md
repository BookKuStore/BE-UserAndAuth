# BookKu App User and Authentication Service

*developer/s*:
- Fadhil Muhammad (2206083464)

## Design Pattern
Ref: https://refactoring.guru/design-patterns/singleton

Service ini menerapkan Singleton Pattern untuk manajemen autentikasi dalam aplikasi ini.

   - Singleton Pattern memastikan bahwa hanya ada satu instance dari kelas manajemen autentikasi yang ada dalam aplikasi pada satu waktu. Hal ini memungkinkan untuk menyimpan informasi sesi pengguna secara konsisten di seluruh aplikasi.
   - Dengan demikian, informasi autentikasi seperti token akses atau status otentikasi dapat diakses secara konsisten di berbagai bagian aplikasi tanpa perlu menyinkronkan status autentikasi di berbagai tempat.
   - Singleton Pattern memungkinkan instance dari kelas autentikasi dapat diakses secara global di seluruh aplikasi. Hal ini berguna karena informasi autentikasi sering dibutuhkan di berbagai bagian aplikasi.



Dengan menggunakan Singleton Pattern untuk autentikasi, kami dapat memastikan konsistensi, efisiensi, dan kemudahan akses dalam manajemen autentikasi dalam aplikasi.

## Software Architecture

Dalam proyek ini, kami menerapkan Arsitektur Perangkat Lunak berbasis Microservices. Kami telah memecah aplikasi kami menjadi beberapa layanan, termasuk (hingga saat ini) Layanan Pengguna & Autentikasi, Layanan Kupon, Layanan Chart & Riwayat, Layanan Buku, dan Layanan Front-End.