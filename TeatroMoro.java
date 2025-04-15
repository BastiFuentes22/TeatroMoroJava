
package com.mycompany.teatromorojava;

// Versión simplificada del sistema Teatro Moro con mejoras visuales y validación por edad
import java.util.Scanner;

public class TeatroMoroApp {

    static int[][] vip = new int[2][8];
    static int[][] plateaBaja = new int[2][8];
    static int[][] plateaAlta = new int[2][8];
    static int[][] palcos = new int[2][8];

    static int totalEntradasVendidas = 0;
    static int totalIngresos = 0;
    static int contadorEntradas = 1;

    static String[] historialBoletas = new String[100]; // máx 100 entradas

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n--- SISTEMA TEATRO MORO 🎭 ---");
            System.out.println("1. Comprar entrada");
            System.out.println("2. Ver plano de asientos");
            System.out.println("3. Mostrar boletas");
            System.out.println("4. Eliminar entrada");
            System.out.println("5. Ver estadísticas");
            System.out.println("6. Salir");
            System.out.print("Selecciona una opción: ");
            while (!sc.hasNextInt()) {
                System.out.print("Por favor, ingresa un número válido: ");
                sc.next();
            }
            opcion = sc.nextInt();

            switch (opcion) {
                case 1 -> comprarEntrada(sc);
                case 2 -> mostrarTodosLosPlanos();
                case 3 -> mostrarBoletas();
                case 4 -> eliminarEntrada(sc);
                case 5 -> verEstadisticas();
                case 6 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 6);
    }

    static void mostrarTodosLosPlanos() {
        mostrarPlano(vip, "VIP");
        mostrarPlano(plateaBaja, "Platea Baja");
        mostrarPlano(plateaAlta, "Platea Alta");
        mostrarPlano(palcos, "Palcos");
    }

    static void comprarEntrada(Scanner sc) {
        System.out.println("\n--- COMPRA DE ENTRADA ---");
        mostrarTodosLosPlanos();

        System.out.println("Zonas: 1. VIP ($30000) | 2. Platea Baja ($15000) | 3. Platea Alta ($18000) | 4. Palcos ($13000)");
        System.out.print("Elige zona (1-4): ");
        int zona = validarRango(sc, 1, 4);

        int[][] sala = switch (zona) {
            case 1 -> vip;
            case 2 -> plateaBaja;
            case 3 -> plateaAlta;
            case 4 -> palcos;
            default -> null;
        };
        String nombreZona = switch (zona) {
            case 1 -> "VIP";
            case 2 -> "Platea Baja";
            case 3 -> "Platea Alta";
            case 4 -> "Palcos";
            default -> "Desconocida";
        };
        int precioBase = switch (zona) {
            case 1 -> 30000;
            case 2 -> 15000;
            case 3 -> 18000;
            case 4 -> 13000;
            default -> 0;
        };

        mostrarPlano(sala, nombreZona);
        System.out.print("Fila (1-2): ");
        int fila = validarRango(sc, 1, 2) - 1;
        System.out.print("Asiento (1-8): ");
        int asiento = validarRango(sc, 1, 8) - 1;

        if (sala[fila][asiento] == 1) {
            System.out.println("⛔ Asiento ya vendido.");
            return;
        }

        System.out.print("Ingresa tu edad: ");
        int edad = validarRango(sc, 1, 120);

        double descuento = 0;
        if (edad <= 18) descuento = 0.10;
        else if (edad >= 60) descuento = 0.15;

        int precioFinal = (int)(precioBase * (1 - descuento));

        sala[fila][asiento] = 1;
        totalEntradasVendidas++;
        totalIngresos += precioFinal;

        String boleta = "Entrada #" + contadorEntradas + " | Zona: " + nombreZona + " | Fila: " + (fila + 1) + " | Asiento: " + (asiento + 1) + " | Precio: $" + precioFinal;
        historialBoletas[contadorEntradas - 1] = boleta;
        contadorEntradas++;

        System.out.println("\n✅ ¡Compra exitosa!");
        System.out.println("📄 BOLETA:");
        System.out.println(boleta);
    }

    static void mostrarPlano(int[][] zona, String nombre) {
        System.out.println("\nPlano de " + nombre + ":");
        for (int f = 0; f < 2; f++) {
            System.out.print("Fila " + (f + 1) + ": ");
            for (int a = 0; a < 8; a++) {
                System.out.print(zona[f][a] == 0 ? "[ ] " : "[X] ");
            }
            System.out.println();
        }
        System.out.println("Leyenda: [ ] = Disponible | [X] = Vendido");
    }

    static void mostrarBoletas() {
        System.out.println("\n--- BOLETAS GENERADAS ---");
        for (int i = 0; i < contadorEntradas - 1; i++) {
            if (historialBoletas[i] != null) {
                System.out.println(historialBoletas[i]);
            }
        }
    }

    static void eliminarEntrada(Scanner sc) {
        System.out.print("\nNúmero de entrada a eliminar: ");
        int numero = validarRango(sc, 1, contadorEntradas - 1);
        if (historialBoletas[numero - 1] == null) {
            System.out.println("Entrada no encontrada.");
            return;
        }

        String[] partes = historialBoletas[numero - 1].split("\\|");
        String zona = partes[1].trim().split(": ")[1];
        int fila = Integer.parseInt(partes[2].trim().split(": ")[1]) - 1;
        int asiento = Integer.parseInt(partes[3].trim().split(": ")[1]) - 1;
        int precio = Integer.parseInt(partes[4].trim().split("\\$")[1]);

        int[][] sala = switch (zona) {
            case "VIP" -> vip;
            case "Platea Baja" -> plateaBaja;
            case "Platea Alta" -> plateaAlta;
            case "Palcos" -> palcos;
            default -> null;
        };

        if (sala != null) {
            sala[fila][asiento] = 0;
            historialBoletas[numero - 1] = null;
            totalIngresos -= precio;
            totalEntradasVendidas--;
            System.out.println("✅ Entrada eliminada.");
        } else {
            System.out.println("Error eliminando entrada.");
        }
    }

    static void verEstadisticas() {
        System.out.println("\n📊 Estadísticas:");
        System.out.println("Total entradas vendidas: " + totalEntradasVendidas);
        System.out.println("Total ingresos: $" + totalIngresos);
    }

    static int validarRango(Scanner sc, int min, int max) {
        int valor;
        do {
            while (!sc.hasNextInt()) {
                System.out.print("Por favor, ingresa un número válido: ");
                sc.next();
            }
            valor = sc.nextInt();
            if (valor < min || valor > max) {
                System.out.print("Valor fuera de rango. Ingresa un número entre " + min + " y " + max + ": ");
            }
        } while (valor < min || valor > max);
        return valor;
    }
}

