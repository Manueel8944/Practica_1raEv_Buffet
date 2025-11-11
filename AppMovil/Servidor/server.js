const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(express.json());

let direccion = 'mongodb://localhost/Buffet';

mongoose.connect(direccion)
.then(() => console.log('Conectado a la base de datos'))
.catch(err => console.error('Error de conexión a la base de datos:', err));

const comidaSchema = new mongoose.Schema({
  id: Number,
  nombre: String,
  precio: Number
});

const mesasSchema = new mongoose.Schema({
  id: Number,
  nombre: String,
  estadoMesa: Boolean,
  estadoPedido: Boolean
});

const pedidosSchema = new mongoose.Schema({
  idMesa: Number,
  comidas: [
    {
      idComida: Number,
      cantidad: Number
    }
  ],
});

const Comida = mongoose.model("Comida", comidaSchema, "comidas");
const Mesa = mongoose.model("Mesa", mesasSchema, "mesas");
const Pedido = mongoose.model("Pedido", pedidosSchema, "pedidos");

app.get('/comidas', async (req, res) => {
  const comidas = await Comida.find();
  res.json(comidas);
});

app.post('/comidas', async (req, res) => {
  const nueva = new Comida(req.body);
  await nueva.save();
  res.json(nueva);
});

app.put('/comidas/:id', async (req, res) => {
  const actualizada = await Comida.findOneAndUpdate({ id: Number(req.params.id) }, req.body, { new: true });
  res.json(actualizada);
});

app.delete('/comidas/:id', async (req, res) => {
  await Comida.findOneAndDelete({ id: Number(req.params.id) });
  res.json({ mensaje: 'Comida eliminada' });
});



app.get('/mesas', async (req, res) => {
  const mesas = await Mesa.find();
  res.json(mesas);
});

app.post('/mesas', async (req, res) => {
  const nueva = new Mesa(req.body);
  await nueva.save();
  res.json(nueva);
});

app.put('/mesas/:id', async (req, res) => {
  const actualizada = await Mesa.findOneAndUpdate({ id: Number(req.params.id) }, req.body, { new: true });
  res.json(actualizada);
});

app.delete('/mesas/:id', async (req, res) => {
  await Mesa.findOneAndDelete({ id: Number(req.params.id) });
  res.json({ mensaje: 'Mesa eliminada' });
});



app.get('/pedidos', async (req, res) => {
  const pedidos = await Pedido.find();
  res.json(pedidos);
});

app.post('/pedidos', async (req, res) => {
  const nuevo = new Pedido(req.body);
  await nuevo.save();
  res.json(nuevo);
});

app.put('/pedidos/:id', async (req, res) => {
  const actualizado = await Pedido.findByIdAndUpdate(req.params.id, req.body, { new: true });
  res.json(actualizado);
});

app.delete('/pedidos/:id', async (req, res) => {
  await Pedido.findByIdAndDelete(req.params.id);
  res.json({ mensaje: 'Pedido eliminado' });
});

const PORT = 3000;
app.listen(PORT, '0.0.0.0', () => console.log(`Servidor corriendo en http://0.0.0.0:${PORT}`));
