Practica-MP-G12/
├── .gitignore              (Ya lo tienes, asegura que ignora la carpeta /out o /bin y archivos .idea)
├── LICENSE                 (Ya lo tienes)
├── README.md               (Ya lo tienes)
├── src/                    (Tu código fuente va aquí)
│   ├── main/               (El código principal del juego)
│   │   └── java/           (Aquí irán vuestros paquetes y clases Java)
│   │       └── urjc/       (Carpeta del paquete raíz, ej: com.urjc.metprog o urjc.metprog)
│   │           └── metprog/
│   │               ├── Main.java           (La clase con el public static void main)
│   │               ├── models/             (Clases de datos: Personaje, Vampiro, Arma, etc.)
│   │               ├── controllers/        (Lógica del juego: CombateController, DesafioController, etc.)
│   │               └── views/              (Si hacéis menús por consola o GUI)
│   │
│   └── test/               (El código de vuestras pruebas unitarias va aquí)
│       └── java/           (Aquí replicaréis la estructura de paquetes para los tests)
│           └── urjc/
│               └── metprog/
│                   └── ...
│
├── docs/                   (Opcional, pero muy recomendado)
│   └── (Aquí podéis guardar los PDFs de la práctica, diagramas UML, etc.)
│
└── data/                   (Si vais a usar persistencia en archivos txt, json, etc., guardadlos aquí)
