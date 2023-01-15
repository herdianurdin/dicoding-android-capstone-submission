## SIB 2022: Capstone Project Team C22-218

![Screenshot](app-banner.webp)

**GOMetry** adalah aplikasi pembelajaran bangun ruang interaktif dengan menggunakan teknologi **Augmented Reality** dan **Konsep Gamifikasi**. Repository ini merupakan salinan dan pembaruan dari repository original proyek akhir tim saya [(GOMetry)](https://github.com/EndKn1ght/gometry).

Berikut adalah daftar pembaruan aplikasi GOMetry:

- Menggunakan design pattern MVVM.
- Perbaikan bug dan issue dari proyek aslinya.
- Pergantian akun firebase.

### Struktur JSON Realtime Database

```JSON
{
  "users": {
    "userId": {
      "achievements": [],
      "displayName": "",
      "email": "",
      "geometries": [],
      "id": "",
      "photoUrl": "",
      "point": 0
    }
  },
  "questions": {
    "questionId": {
      "anwer": "",
      "geometryId": "",
      "image": "",
      "level": "",
      "option": [],
      "question": ""
    }
  }
}
```

### Dibuat Dengan

[<img src='android.svg' alt='android' width='100' />](https://www.android.com/)
[<img src='kotlin.svg' alt='kotlin' width='100' />](https://kotlinlang.org/)
[<img src='firebase.svg' alt='firebase' width='100' />](https://firebase.google.com/)
[<img src='arcore.svg' alt='ARCore' width='100' />](https://developers.google.com/ar)

## License

Distributed under the MIT License. See [LICENSE](LICENSE) for more information.
