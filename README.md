# FRV-Challenge - Test Automation Framework

## Descripción

Este proyecto implementa un framework de automatización de pruebas para la plataforma Frávega. El enfoque principal -el caso de uso crítico que se eligió- es validar promociones de cuotas sin interés por tarjeta y banco. Se optimiza para escalabilidad, integración CI/CD y ejecuciones paralelas.  Leer la documentación final para entender decisiones técnicas, arquitectura, limitaciones y conclusiones.

## Tecnologías utilizadas

* **Java**
* **TestNG** para gestión de tests
* **Selenium WebDriver**
* **Maven** como herramienta de building
* **Jenkins** para integración continua
* **WebDriverManager** para la resolución automática de web drivers
* **Log4j + SLF4J** para logging
* **Allure Reports** para reportes avanzados

## Estructura del proyecto

```
FRV-Challenge/
├── src/main/java/
│   ├── webdriver/                 # WebDriverFactory
│   ├── fravega/actions/           # Acciones comunes reutilizables
│   └── fravega/utils/             # Logger util
├── src/test/java/
│   ├── fravega/base/              # Base test class y configuración paralela
│   ├── fravega/tests/             # Casos de prueba
│   ├── fravega/pages/             # Page Objects
│   ├── fravega/dataproviders/     # Data Providers (JSON)
│   └── fravega/utils/             # Helpers, Listeners personalizados (Allure)
├── testng/                        # Archivos de suite TestNG XML
├── Jenkinsfile                    # Pipeline declarativa Jenkins
├── pom.xml                        # Configuración Maven y dependencias
```

## Ejecutar los tests localmente

### Prerrequisitos:

* Java 17+
* Maven 3+
* Google Chrome

### Comando de ejecución

```bash
mvn clean test -DsuiteXmlFile=testng/SmokeSuite.xml
```

## Ver reportes Allure

1. Ejecutar los tests con Maven
2. Generar y ver el reporte:

```bash
allure serve target/allure-results
```

## Integración Jenkins

La pipeline se activa con cada push a la rama `feature-cuotas`. Incluye:

* Checkout del repositorio
* Ejecución de test suite con Maven
* Generación de reportes Allure

## Modo Headless

Automatizado al detectar entorno Jenkins mediante parámetro XML:

```xml
<parameter name="executionEnv" value="jenkins"/>
```

## Ejecución paralela

Definida en el suite XML:

```xml
<suite name="Smoke Suite" parallel="methods" thread-count="3">
```

---

*Para detalles técnicos y decisiones de diseño, ver el documento [Documentación Técnica](https://github.com/JAcciarri/FRV-Challenge/blob/main/Documentacion%20Tecnica.md).*
