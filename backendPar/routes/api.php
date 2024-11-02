<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

use App\Http\Controllers\ProductController;

Route::get('/products', [ProductController::class, 'getProducts']);
Route::delete('/products/delete/{id}', [ProductController::class, 'deleteProduct']);
Route::put('/product/update/{id}', [ProductController::class, 'updateProduct']);
Route::post('/product/create', [ProductController::class, 'createProduct']);

Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
    return $request->user();
});
