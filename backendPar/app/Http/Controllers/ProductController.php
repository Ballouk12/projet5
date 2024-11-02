<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\ProductModel;

class ProductController extends Controller
{
    public function getProducts()
    {
        $products = ProductModel::all()->map(function ($product) {
            return [
                'id' => $product->id,
                'title' => $product->title,
                'description' => $product->description,
                'price' => $product->price,
                'image' => base64_encode($product->image), 
                
            ];
        });

        return response()->json($products); 
    }

    public function deleteProduct($id)
    {
        $product = ProductModel::find($id);
    
        if ($product) {
            $product->delete();
            return response()->json(['message' => 'Produit supprimé avec succès.'], 200);
        }
    
        return response()->json(['message' => 'Produit non trouvé.'], 404);
    }

    public function updateProduct(Request $request, $id)
{
    // Validation des données
    $validatedData = $request->validate([
        'title' => 'required|string|max:255',
        'price' => 'required|numeric',
        'image' => 'nullable|string', // Image en base64 sous forme de chaîne de caractères
    ]);

    // Récupération du produit à mettre à jour
    $product = ProductModel::find($id);

    // Vérification si le produit existe
    if ($product) {
        // Mise à jour des champs du produit
        $product->title = $validatedData['title'];
        $product->price = $validatedData['price'];
        $product->description = "gggggggggggg";

        // Si l'image est présente dans la requête, on la décode du format base64
        if (!empty($validatedData['image'])) {
            $imageData = base64_decode($validatedData['image']); // Décodage base64
            $product->image = $imageData; // Stockage en tant que BLOB
        }

        // Sauvegarde des changements dans la base de données
        $product->save();

        return response()->json(['message' => 'Produit mis à jour avec succès.'], 200);
    }

    return response()->json(['message' => 'Produit non trouvé.'], 404);
}

public function createProduct(Request $request)
{
    // Validation des données
    $validatedData = $request->validate([
        'title' => 'required|string|max:255',
        'price' => 'required|numeric',
        'image' => 'nullable|string', // Image en base64 sous forme de chaîne de caractères
    ]);

    // Création d'un nouveau produit
    $product = new ProductModel();

    // Assignation des champs
    $product->title = $validatedData['title'];
    $product->price = $validatedData['price'];
    $product->description = "gggggggggggg";

    // Si l'image est présente dans la requête, on la décode du format base64
    if (!empty($validatedData['image'])) {
        $imageData = base64_decode($validatedData['image']); // Décodage base64
        $product->image = $imageData; // Stockage en tant que BLOB
    }

    // Sauvegarde dans la base de données
    $product->save();

    return response()->json(['message' => 'Produit créé avec succès.'], 201);
}


    
}
