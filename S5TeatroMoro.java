
package com.mycompany.s5teatromoro;

import java.util.Scanner;

public class S5TeatroMoro {

    static int[][] asientos = new int[8][8];
    static int contadorEntradas = 1;

    static String[] historialBoletas = new String[100];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("---TEATRO MORO---");
            System.out.println("Función: Edo Caroe, 17 de abril, 20;30");
            System.out.println("Menú: ");
            System.out.println("1. Comprar entradas");
            System.out.println("2. Ver plano de asientos");
            System.out.println("3. Mostrar boletas");
            System.out.println("4. Salir");
            System.out.print("Selecciona una opción: ");
            while (!sc.hasNextInt()) {
                System.out.print("Por favor, ingresa un número válido: ");
                sc.next();
            }
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:{
                    comprarEntrada(sc);
                }   break;
                case 2:{
                    mostrarPlanoCompleto();
                }   break;
                case 3:{
                    mostrarBoletas();
                }   break;
              
                case 4:{
                    System.out.println("Saliendo del sistema...");
                }   break;
                default:{
                    System.out.println("Opción inválida.");
                }   break;
            }
        } while (opcion != 5);
    }

    static void mostrarPlanoCompleto() {
        System.out.println(". . . PLANO COMPLETO DE ASIENTOS . . .");
        for (int f = 0; f < 8; f++) {
            String zona = obtenerZonaPorFila(f);
            System.out.print("Fila " + (f + 1) + " (" + zona + "): ");
            for (int a = 0; a < 8; a++) {
                System.out.print(asientos[f][a] == 0 ? "[ ] " : "[X] ");
            }
            System.out.println();
        }
        System.out.println(" [ ] = Disponible | [X] = Vendido");
    }

    static void comprarEntrada(Scanner sc) {
        System.out.println(". . . COMPRA DE ENTRADAS . . .");
        mostrarPlanoCompleto();

        System.out.print("¿Cuántas entradas deseas comprar?: ");
        int cantidad = validarRango(sc, 1, 10);

        StringBuilder boletaAgrupada = new StringBuilder();
        int totalCompra = 0;

        for (int i = 0; i < cantidad; i++) {
            System.out.println("Entrada " + (i + 1) + ":");
            System.out.print("Selecciona la fila (1-8): ");
            int fila = validarRango(sc, 1, 8) - 1;
            System.out.print("Selecciona el número de asiento (1-8): ");
            int asiento = validarRango(sc, 1, 8) - 1;

            if (asientos[fila][asiento] == 1) {
                System.out.println("Asiento vendido. Elige otro.");
                i--;
                continue;
            }

            System.out.print("Ingresa tu edad: ");
            int edad = validarRango(sc, 1, 120);

            String tipoCliente = "General";
            double descuento = 0;
            if (edad <= 18) {
                descuento = 0.10;
                tipoCliente = "Estudiante";
            } else if (edad >= 60) {
                descuento = 0.15;
                tipoCliente = "Tercera Edad";
            }

            String zona = obtenerZonaPorFila(fila);
            int precioBase = obtenerPrecioPorZona(zona);
            int precioFinal = (int)(precioBase * (1 - descuento));

            asientos[fila][asiento] = 1;

            String linea = "Entrada #" + contadorEntradas + " || Zona: " + zona + " || Fila: " + (fila + 1) + " || Asiento: " + (asiento + 1) + " || Tipo: " + tipoCliente + " || Precio: $" + precioFinal;
            historialBoletas[contadorEntradas - 1] = linea;
            contadorEntradas++;

            boletaAgrupada.append(linea).append("");
            totalCompra += precioFinal;
        }

        System.out.println("Compra completa. Boleta agrupada:");
        System.out.println(boletaAgrupada);
        System.out.println("Total pagado: $" + totalCompra);
    }

    static String obtenerZonaPorFila(int fila) {
        if (fila >= 0 && fila <= 1){
            return "VIP";
        }else if (fila <= 3){
            return "Platea Baja";
        }else if (fila <= 5){
            return "Platea Alta";
        }else if (fila <= 7) {  
            return "Palcos";
        }else return "Desconocida";
}

    

    static int obtenerPrecioPorZona(String zona) {
        return switch (zona) {
            case "VIP" -> 30000;
            case "Platea Baja" -> 15000;
            case "Platea Alta" -> 18000;
            case "Palcos" -> 13000;
            default -> 0;
        };
    }

    static void mostrarBoletas() {
        System.out.println(". . . BOLETAS GENERADAS . . .");
        for (int i = 0; i < contadorEntradas - 1; i++) {
            if (historialBoletas[i] != null) {
                System.out.println(historialBoletas[i]);
            }
        }
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
