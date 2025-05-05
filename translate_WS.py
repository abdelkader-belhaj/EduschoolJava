from flask import Flask, request, jsonify
from flask_cors import CORS  # Pour gérer les requêtes cross-origin
from googletrans import Translator

app = Flask(__name__)
CORS(app)  # Active CORS pour toutes les routes

@app.route('/translate', methods=['POST'])
def translate():
    data = request.get_json()
    
    # Vérifiez que les données sont valides
    if not data or 'text' not in data or 'lang' not in data:
        return jsonify({'error': 'Invalid request'}), 400

    text = data['text']
    target_language = data['lang']

    # Utilisez googletrans pour traduire le texte
    translator = Translator()
    try:
        translation = translator.translate(text, dest=target_language)
        translated_text = translation.text
    except Exception as e:
        return jsonify({'error': str(e)}), 500

    return jsonify({'translated_text': translated_text})

if __name__ == '__main__':
    app.run(host='127.0.0.1', port=5000, debug=True)