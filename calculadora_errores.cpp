// ARCHIVO CON ERRORES INTENCIONALES - Encuentra todos los errores!
// Pista: hay exactamente 8 errores distribuidos en el codigo.

#include <iostream>
#include <cmath>
using namespace std;

void mostrarMenu() {
    cout << "\n===== CALCULADORA CIENTIFICA C++ =====" << endl;
    cout << "1. Suma" << endl;
    cout << "2. Resta" << endl;
    cout << "3. Multiplicacion" << endl;
    cout << "4. Division" << endl;
    cout << "5. Potencia" << endl;
    cout << "6. Raiz cuadrada" << endl;
    cout << "7. Seno" << endl;
    cout << "8. Coseno" << endl;
    cout << "9. Logaritmo natural" << endl;
    cout << "0. Salir" << endl;
    cout << "Seleccione una opcion: ";
}

double sumar(double a, double b) {
    return a + b;
}

double restar(double a, double b) {
    return a - b;  
}

double multiplicar(double a, double b) {
    return a * b;
}

double dividir(double a, double b) {
    if (b = 0) {                          // ERROR 1
        cout << "Error: Division por cero." << endl;
        return 0;
    }
    return a / b;
}

double potencia(double base, double exp) {
    return pow(base, exp);
}

double raizCuadrada(double a) {
    if (a < 0) {
        cout << "Error: Numero negativo." << endl;
        return -1;
    }
    return sqrt(a)
}                                          // ERROR 2

double seno(double grados) {
    return sin(grados);                    // ERROR 3
}

double coseno(double grados) {
    return cos(grados * 3.14159 / 180.0);
}

double logaritmo(double a) {
    if (a <= 0) {
        cout << "Error: Solo numeros positivos." << endl;
        return -1;
    }
    return log(a);
}

int main() {
    int opcion;
    double a, b;

    do {
        mostrarMenu();
        cin >> opcion;

        switch (opcion) {
            case 1:
                cout << "Ingrese dos numeros: ";
                cin >> a >> b;
                cout << "Resultado: " << sumar(a, b) << endl;
                breack;                    // ERROR 4

            case 2:
                cout << "Ingrese dos numeros: ";
                cin >> a >> b;
                cout << "Resultado: " << restar(a, b) << endl;
                break;

            case 3:
                cout << "Ingrese dos numeros: ";
                cin >> a >> b;
                cout << "Resultado: " << multiplicar(a, b) << endl;
                break;

            case 4:
                cout << "Ingrese dos numeros: ";
                cin >> a >> b;
                cout << "Resultado: " << dividir(a, b) << endl;
                break;

            case 5:
                cout << "Ingrese base y exponente: ";
                cin >> a >> b;
                cout << "Resultado: " << potencia(a, b) << endl;
                break;

            case 6:
                cout << "Ingrese un numero: ";
                cin >> a;
                cout << "Resultado: " << raizCuadrada(a) << endl;
                break;

            case 7:
                cout << "Ingrese el angulo en grados: ";
                cin >> a;
                cout << "Resultado: " << seno(a) << endl;
                break;

            case 8:
                cout << "Ingrese el angulo en grados: ";
                cin >> a;
                cout << "Resultado: " << coseno(a) << endl;
                break;

            case 9:
                cout << "Ingrese un numero positivo: ";
                cin >> a;
                cout << "Resultado: " << logaritmo(a) << endl;
                break;

            case 0:
                cout << "Saliendo..." << endl;
                Break;                     // ERROR 5

            defalt:                        // ERROR 6
                cout << "Opcion invalida." << endl;
        }

    } while (opcion =! 0);                 // ERROR 7  ERROR 8

    return 0;
}
