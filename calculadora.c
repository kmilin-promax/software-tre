#include <stdio.h>
#include <math.h>
#include <stdlib.h>

void mostrarMenu() {
    printf("\n===== CALCULADORA CIENTIFICA =====\n");
    printf("1. Suma\n");
    printf("2. Resta\n");
    printf("3. Multiplicacion\n");
    printf("4. Division\n");
    printf("5. Potencia\n");
    printf("6. Raiz cuadrada\n");
    printf("7. Seno\n");
    printf("8. Coseno\n");
    printf("9. Logaritmo natural\n");
    printf("10. Logaritmo base 10\n");
    printf("0. Salir\n");
    printf("Seleccione una opcion: ");
}

int main() {
    int opcion;
    double a, b, resultado;

    do {
        mostrarMenu();
        scanf("%d", &opcion);

        switch (opcion) {
            case 1:
                printf("Ingrese dos numeros: ");
                scanf("%lf %lf", &a, &b);
                resultado = a + b;
                printf("Resultado: %.4f\n", resultado);
                break;
            case 2:
                printf("Ingrese dos numeros: ");
                scanf("%lf %lf", &a, &b);
                resultado = a - b;
                printf("Resultado: %.4f\n", resultado);
                break;
            case 3:
                printf("Ingrese dos numeros: ");
                scanf("%lf %lf", &a, &b);
                resultado = a * b;
                printf("Resultado: %.4f\n", resultado);
                break;
            case 4:
                printf("Ingrese dos numeros: ");
                scanf("%lf %lf", &a, &b);
                if (b == 0) {
                    printf("Error: Division por cero.\n");
                } else {
                    resultado = a / b;
                    printf("Resultado: %.4f\n", resultado);
                }
                break;
            case 5:
                printf("Ingrese base y exponente: ");
                scanf("%lf %lf", &a, &b);
                resultado = pow(a, b);
                printf("Resultado: %.4f\n", resultado);
                break;
            case 6:
                printf("Ingrese un numero: ");
                scanf("%lf", &a);
                if (a < 0) {
                    printf("Error: No se puede calcular raiz de numero negativo.\n");
                } else {
                    resultado = sqrt(a);
                    printf("Resultado: %.4f\n", resultado);
                }
                break;
            case 7:
                printf("Ingrese el angulo en grados: ");
                scanf("%lf", &a);
                resultado = sin(a * M_PI / 180.0);
                printf("Resultado: %.4f\n", resultado);
                break;
            case 8:
                printf("Ingrese el angulo en grados: ");
                scanf("%lf", &a);
                resultado = cos(a * M_PI / 180.0);
                printf("Resultado: %.4f\n", resultado);
                break;
            case 9:
                printf("Ingrese un numero: ");
                scanf("%lf", &a);
                if (a <= 0) {
                    printf("Error: El logaritmo solo se define para numeros positivos.\n");
                } else {
                    resultado = log(a);
                    printf("Resultado: %.4f\n", resultado);
                }
                break;
            case 10:
                printf("Ingrese un numero: ");
                scanf("%lf", &a);
                if (a <= 0) {
                    printf("Error: El logaritmo solo se define para numeros positivos.\n");
                } else {
                    resultado = log10(a);
                    printf("Resultado: %.4f\n", resultado);
                }
                break;
            case 0:
                printf("Saliendo...\n");
                break;
            default:
                printf("Opcion invalida.\n");
        }
    } while (opcion != 0);

    return 0;
}
