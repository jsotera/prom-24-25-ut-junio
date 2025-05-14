<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editor de Mapas</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <h1 class="center">Editor de Mapas</h1>

    <div class="selector-rutas center">
        <form action="/seleccionar-mapa" method="POST">
            <select name="mapaEditable">
                <#assign isSelected=""/>
                <#if mapaSeleccionado=="Ruta 1">
                    <#assign isSelected="selected"/>
                </#if>
                <option ${isSelected} value="Ruta 1">Ruta 1</option>
                <#assign isSelected=""/>
                <#if mapaSeleccionado=="javi">
                    <#assign isSelected="selected"/>
                </#if>
                <option ${isSelected} value="javi">javi</option>
            </select>
            <input type="submit" value="cambiar">
        </form>
    </div>
    
    <p class="center">Selecciona una imagen y haz clic en una celda para colocarla.</p>

    <div class="contenedor">
        <div class="galeria">
            <#list imagenes as imagen>
                <div style="background-image:url('${imagen}');" class="sprite celda" onclick="javascript:seleccionarSprite(this);"></div>
            </#list>
        </div>
    </div>

    <div class="center botones">
        <button onclick="javascript:mostrarOcultarGrid(this);">Ocultar grid</button>
    </div>

    <div id="tablero" class="tablero show-border">
        <div>
            <#list 0..(tablero?size-1) as fila>
                <div class="fila">
                    <#list 0..(tablero[fila]?size-1) as columna>
                        <div class="celda-wrapper">
                            <div onclick="javascript: colocarSprite(this);" onmouseover="javascript: ratonSobreCelda(this);" class="celda" data-fila="${fila}" data-columna="${columna}" style="background-image: url('${tablero[fila][columna]}');"></div>
                        </div>
                    </#list>
                </div>
            </#list>
        </div>
    </div>

    <script>
        var celdas = document.querySelectorAll('.celda');
        var imagenSeleccionada = "";
        var isMouseDown = false;

        // Detectar clic y soltar
        document.addEventListener('mousedown', function () {
            isMouseDown = true;
        });

        document.addEventListener('mouseup', function () {
            isMouseDown = false;
        });

        function seleccionarSprite(sprite){
            var spriteSeleccionado = document.querySelector('.seleccionada');
            if(spriteSeleccionado){
                spriteSeleccionado.classList.remove('seleccionada');
            }
            sprite.classList.add('seleccionada');
            console.log(sprite.style.backgroundImage);
            imagenSeleccionada = sprite.style.backgroundImage;
        }

        function ratonSobreCelda(celda){
            if (isMouseDown) {
                colocarSprite(celda);
            }
        }

        function colocarSprite(celda) {
            var fila = celda.getAttribute('data-fila');
            var columna = celda.getAttribute('data-columna');
            console.log("pintando sobre ("+fila+", "+columna+")");
            celda.style.backgroundImage = imagenSeleccionada;
            enviarDatos(fila, columna, imagenSeleccionada);
        };

        function mostrarOcultarGrid(boton){
            var tablero = document.getElementById("tablero");
            if (tablero.classList.contains("show-border")) {
                tablero.classList.remove("show-border");
                boton.textContent = "Mostrar grid";
            } else {
                tablero.classList.add("show-border");
                boton.textContent = "Ocultar grid";
            }
        }

        function enviarDatos(fila, columna, sprite){
            fetch("/colocarSprite", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                },                
                body: 'fila='+encodeURIComponent(fila)+'&columna='+encodeURIComponent(columna)+'&sprite='+encodeURIComponent(sprite)
            });
        }

    </script>
</body>
</html>
