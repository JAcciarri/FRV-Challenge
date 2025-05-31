
# Documentacion Tecnica - FRV Challenge

## 1. Caso de uso crítico: Promociones de cuotas por tarjetas y bancos. Test de validacion parametrizada por productos y cantidad de cuotas.

### Explicación breve de caso de uso

El escenario de promociones de cuotas sin interés representa una funcionalidad clave para la experiencia del usuario en el ecommerce de Frávega. Validar esto correctamente asegura que las condiciones comerciales se visualicen de forma clara, consistente y sin errores.  
El punto importante para mí es que un usuario espera poder utilizar las cuotas sin interés independientemente de la tarjeta que tenga o del banco que use, estos scripts automatizados validan de manera muy rápida si alguna promoción para algún banco o alguna tarjeta no son las esperadas. ¿Qué significa esto? Que los intereses totales sean igual a \$0, que el precio financiado total sea igual al original, que las tarjetas que se exhiben al usuario como prometedoras de cuotas sin interés realmente lo sean una vez seleccionadas, entre otras.

Un error en este escenario puede directamente perder un potencial cliente o una venta perdida por la frustración de que las cuotas sean con interés, o que su tarjeta o banco no las ofrezcan realmente cuando claramente estaban disponibles al visualizar el producto.

### Justificación de Automatización

Este caso de uso es ideal para la automatización porque realizarlo de manera manual sería demasiado extenso. Hoy en día Frávega ofrece alrededor de 5 tarjetas diferentes, para las cuales se ofrecen múltiples bancos (entre 6 y 12) para CADA UNA de ellas. Validar manualmente que:  

- Las tarjetas ofrecidas en la vista individual de un producto sean las mismas que se ofrecen en el modal de 'Promociones bancarias'
- Por otro lado, hay que verificar que para múltiples combinaciones de tarjeta y banco, el sistema efectivamente respete las condiciones de cuotas sin interés, es decir, que el precio final no cambie con respecto al valor en efectivo y no aparezcan intereses.

Es un trabajo no solo tedioso sino además muy propenso a errores humanos. Para esto planifiqué, diseñé, construí e implementé una solución robusta, altamente escalable, de ejecución paralela, integrada con Jenkins, parametrizada para las cuotas que se deseen, parametrizada para ser alimentada con data sets de diferentes productos y cuotas, además lista para ser extendida para validar solo tarjetas deseadas, solo bancos deseados, o cualquier regla de negocio de interés.

### Flujo real en Frávega propuesto para esta solución

Como en la mayoría de las empresas, los desarrolladores de la aplicación web deben tener el código fuente en un repositorio centralizado (o varios) con control de versiones, por ejemplo Git - GitHub/GitLab.  
Este Framework de Automatización propone (y permite) que ante cambios en alguna branch específica como `feature-cuotas`, Jenkins dispare automáticamente una ejecución utilizando pipelines declarativas. Esto asegura feedback temprano en desarrollo y despliegue continuo. Los reportes son claros, fáciles de entender, altamente configurables, muestran capturas de pantalla ante un error de un test para ver dónde es que falló. Además, Jenkins mantiene un historial de ejecuciones para ver tendencias, estadísticas de ejecuciones, y demás datos de interés, todo centralizado. Gracias a la integración con Allure, sería muy simple extender la solución para generar un email y enviarlo automáticamente a stakeholders en un formato visual que es perfectamente entendible. (Leer futuras mejoras).

## 2. Decisiones técnicas clave

- **Selenium WebDriver**: para interacción a nivel browser con el sitio.
- **TestNG**: por su potencia para la definición de suites, data providers y paralelismo.
- **Page Object Model + Factory Pattern**: para desacoplar lógica de negocio y facilitar mantenimiento.
- **Jenkins**: para CI/CD, con pipelines disparadas en ramas específicas (`feature-cuotas`).
- **Allure Reports**: para trazabilidad visual de los resultados, incluyendo screenshots automáticos en fallas.
- **Ejecución headless condicional**: activado solo en entorno Jenkins para optimizar integraciones.
- **WebDriverManager**: para eliminar dependencias externas en el manejo de drivers.

## 3. Arquitectura general del framework

- **Test Layer**: clases de prueba dentro de `fravega.tests`.
- **Base Layer**: inicialización del `WebDriver` y configuración paralela.
- **Page Layer**: implementación de Page Objects representando entidades clave de la UI.
- **Fravega Common Actions**: métodos comunes para ser reutilizados por los Page Objects en el framework de automatización.
- **Helpers y Enums**: para encapsular valores constantes, rutas de imagen, y abstracciones de negocio.
- **Data Layer**: JSON externos cargados vía `DataProvider` para demostrar desacople de datos (data-driven).
- **Utils**: logger, capturador de screenshots, parsers.

## 4. Limitaciones

- No se implementó BrowserStack u otro proveedor de nube.
- Soporte validado en Chrome: aunque el framework ya está configurado y soporta múltiples navegadores (Chrome, Firefox, Edge), las pruebas fueron ejecutadas principalmente sobre Chrome para esta implementación inicial.

## 5. Futuras mejoras

- **Cross Browser-Platform Testing**: integración con servicios como BrowserStack para validar en múltiples navegadores y plataformas.
- **Mayor soporte de datos**: parametrización avanzada vía archivos CSV o conexión con base de datos.
- **Notificaciones automatizadas**: envío por Slack o Email de reportes Allure generados tras ejecución.

## 6. Capturas de pantalla 

En la carpeta screenshots se encuentran imágenes que documentan el proceso de desarrollo y configuración del proyecto. Estas capturas incluyen:

- Configuración de Jenkins y creación de pipelines

- Ejecución de tests automatizados

- Visualización de resultados en Allure Reports

- Validaciones del caso de uso crítico con errores simulados
