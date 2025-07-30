function AbrirMenu() {
    document.getElementById("navegacion").style.left = "0";
}

document.getElementById("cerrar").addEventListener("click", function () {
    document.getElementById("navegacion").style.left = "-250px"
});

const ctx = document.getElementById('graficaTickets').getContext('2d');

// Crear degradado azul (horizontal)
const gradienteAzul = ctx.createLinearGradient(0, 0, 600, 0);
gradienteAzul.addColorStop(0, 'rgb(99, 102, 241)');
gradienteAzul.addColorStop(1, 'rgb(59, 130, 246)');

// Datos por año
const datosPorAnio = {
    2023: {
        completados: [12, 18, 22, 30, 25, 28, 31, 20, 22, 18, 15, 10],
        enProceso:   [5,  8,  10, 6,  7,  10, 8,  12, 9,  7,  4,  3],
        anulados:    [1,  2,  1,  3,  4,  2,  3,  1,  2,  1,  1,  2],
    },
    2024: {
        completados: [15, 20, 30, 40, 38, 36, 32, 28, 25, 22, 20, 18],
        enProceso:   [6,  7,  8,  9,  8,  7,  6,  5,  4,  3,  2,  1],
        anulados:    [2,  3,  1,  2,  3,  1,  2,  3,  1,  2,  3,  1],
    },
    2025: {
        completados: [10, 12, 15, 18, 20, 22, 24, 26, 28, 30, 25, 20],
        enProceso:   [3,  4,  5,  6,  5,  4,  3,  2,  3,  4,  5,  6],
        anulados:    [1,  1,  2,  1,  2,  1,  2,  1,  2,  1,  2,  1],
    }
};

// Crear gráfica
let grafica = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: [
            'Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun',
            'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'
        ],
        datasets: [
            {
                label: 'Completados',
                data: datosPorAnio[2024].completados,
                backgroundColor: gradienteAzul
            },
            {
                label: 'En Proceso',
                data: datosPorAnio[2024].enProceso,
                backgroundColor: '#f59e0b'
            },
            {
                label: 'Anulados',
                data: datosPorAnio[2024].anulados,
                backgroundColor: '#ef4444'
            }
        ]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false, // para que puedas controlar el alto
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});

// Función para cambiar año
function actualizarGrafica() {
    const anioSeleccionado = document.getElementById('anio').value;
    grafica.data.datasets[0].data = datosPorAnio[anioSeleccionado].completados;
    grafica.data.datasets[1].data = datosPorAnio[anioSeleccionado].enProceso;
    grafica.data.datasets[2].data = datosPorAnio[anioSeleccionado].anulados;
    grafica.update();
}

//Rendimiento circular
// Plugin para mostrar el porcentaje en el centro de la gráfica
const textoCentroPlugin = {
  id: 'textoCentro',
  beforeDraw(chart) {
    const { width } = chart;
    const { height } = chart;
    const ctx = chart.ctx;
    ctx.restore();
    const fontSize = (height / 114).toFixed(2);
    ctx.font = `${fontSize}em sans-serif`;
    ctx.textBaseline = 'middle';

    const total = chart.data.datasets[0].data.reduce((a, b) => a + b, 0);
    const completados = chart.data.datasets[0].data[0];
    const porcentaje = Math.round((completados / total) * 100);

    const text = `${porcentaje}%`;
    const textX = Math.round((width - ctx.measureText(text).width) / 2);
    const textY = height / 2;

    ctx.fillText(text, textX, textY);
    ctx.save();
  }
};

// Gráfica circular



