{
  description = "Machine Learning Shell Environment";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-24.05";
  };

  outputs = { self, nixpkgs }:
    let
      system = "x86_64-linux";
      pkgs = import nixpkgs { inherit system; };
    in
    {
      devShells.default = pkgs.mkShell {
        buildInputs = with pkgs; [
          zsh
          python311
          python311Packages.pip
          python311Packages.tensorflow
          python311Packages.scikit-learn
          python311Packages.numpy
          python311Packages.pandas
          python311Packages.matplotlib
          python311Packages.keras
        ];
      };
    };
}
